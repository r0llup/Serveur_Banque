/**
 * BanqueWorkerRunnable
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import javax.net.ssl.SSLSocket;
import org.xml.sax.InputSource;
import protocols.ProtocolMAMP;

/**
 * Manage a {@link BanqueWorkerRunnable}
 * @author Sh1fT
 */
public class BanqueWorkerRunnable implements Runnable {
    private Serveur_Banque parent;
    private SSLSocket cSocket;

    /**
     * Create a new {@link BanqueWorkerRunnable} instance
     * @param parent
     * @param cSocket 
     */
    public BanqueWorkerRunnable(Serveur_Banque parent, SSLSocket cSocket) {
        this.setParent(parent);
        this.setcSocket(cSocket);
    }

    public Serveur_Banque getParent() {
        return parent;
    }

    public void setParent(Serveur_Banque parent) {
        this.parent = parent;
    }

    public SSLSocket getcSocket() {
        return cSocket;
    }

    public void setcSocket(SSLSocket cSocket) {
        this.cSocket = cSocket;
    }

    public void run() {
        try {
            this.getParent().getClientLabel().setText(
                    this.getcSocket().getInetAddress().getHostAddress());
            InputSource is = new InputSource(new InputStreamReader(
                    this.getcSocket().getInputStream()));
            BufferedReader br = new BufferedReader(is.getCharacterStream());
            ObjectOutputStream oos = new ObjectOutputStream(
                    this.getcSocket().getOutputStream());
            String cmd = br.readLine();
            if (cmd.contains("TRANSFER_POGN")) {
                Double somme = Double.parseDouble(cmd.split(":")[1]);
                String nomClient = cmd.split(":")[2];
                Integer res = this.getParent().getProtocolMAMP().transferPogn(
                        somme, nomClient);
                switch (res) {
                    case ProtocolMAMP.RESPONSE_OK:
                        oos.writeObject("OK");
                        break;
                    case ProtocolMAMP.RESPONSE_KO:
                        oos.writeObject("KO");
                        break;
                    default:
                        oos.writeObject("KO");
                        break;
                }
            }
            oos.close();
            br.close();
            this.getcSocket().close();
            this.getParent().getClientLabel().setText("aucun");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
    }
}