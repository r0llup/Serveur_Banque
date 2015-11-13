/**
 * DemarrerServeur2
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of Serveur_Banque.
 *
 * Serveur_Banque is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Serveur_Banque is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Serveur_Banque; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package serveur_banque;

import java.io.IOException;
import java.net.BindException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Manage a {@link DemarrerServeur2}
 * @author Sh1fT
 */
public class DemarrerServeur2 extends Thread {
    private Serveur_Banque parent;
    private SSLServerSocket sSocket;
    private SSLSocket cSocket;
    private Boolean stop;

    /**
     * Create a new {@link DemarrerServeur2} instance
     * @param parent 
     */
    public DemarrerServeur2(Serveur_Banque parent) {
        this.setParent(parent);
        this.setsSocket(null);
        this.setcSocket(null);
        this.setStop(false);
    }

    public Serveur_Banque getParent() {
        return this.parent;
    }

    public void setParent(Serveur_Banque parent) {
        this.parent = parent;
    }

    public SSLServerSocket getsSocket() {
        return sSocket;
    }

    public void setsSocket(SSLServerSocket sSocket) {
        this.sSocket = sSocket;
    }

    public SSLSocket getcSocket() {
        return cSocket;
    }

    public void setcSocket(SSLSocket cSocket) {
        this.cSocket = cSocket;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    @Override
    public void run() {
        try {
            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            this.setsSocket((SSLServerSocket)sslserversocketfactory.
                    createServerSocket(this.getParent().getServerPort()));
            final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
            this.getsSocket().setEnabledCipherSuites(enabledCipherSuites);
            while (!this.getStop()) {
                this.setcSocket((SSLSocket) this.getsSocket().accept());
                new Thread(
                    new BanqueWorkerRunnable(
                        this.getParent(), this.getcSocket())
                ).start();
            }
        } catch (BindException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            this.setStop(true);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            this.setStop(true);
            System.exit(1);
        }
    }
}