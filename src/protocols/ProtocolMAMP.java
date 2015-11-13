/**
 * ProtocolMAMP
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

package protocols;

import beans.BeanDBAccessMySQL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import serveur_banque.Serveur_Banque;

/**
 * Manage a {@link ProtocolGIMP}
 * @author Sh1fT
 */
public class ProtocolMAMP implements interfaces.ProtocolMAMP {
    private Serveur_Banque parent;
    private BeanDBAccessMySQL bdbam;
    public static final int RESPONSE_OK = 100;
    public static final int RESPONSE_KO = 600;

    /**
     * Create a new {@link ProtocolMAMP} instance
     * @param parent 
     */
    public ProtocolMAMP(Serveur_Banque parent) {
        this.setParent(parent);
        this.setBdbam(new BeanDBAccessMySQL(
                System.getProperty("file.separator") +"properties" +
                System.getProperty("file.separator") + "BeanDBAccessMySQL.properties"));
    }

    public Serveur_Banque getParent() {
        return parent;
    }

    public void setParent(Serveur_Banque parent) {
        this.parent = parent;
    }

    public BeanDBAccessMySQL getBdbam() {
        return bdbam;
    }

    public void setBdbam(BeanDBAccessMySQL bdbam) {
        this.bdbam = bdbam;
    }

    /**
     * Transfère le montant de la réservation
     * @param somme
     * @param nomClient
     * @return 
     */
    @Override
    public Integer transferPogn(Double somme, String nomClient) {
        try {
            String query = "INSERT INTO transactions VALUES(0, ?, ?);";
            PreparedStatement ps = this.getBdbam().getDBConnection().prepareStatement(query);
            ps.setDouble(1, somme);
            ps.setString(2, nomClient);
            Integer rss = this.getBdbam().executeUpdate(ps);
            if (rss == 1) {
                this.getBdbam().getDBConnection().commit();
                return ProtocolMAMP.RESPONSE_OK;
            } else
                return ProtocolMAMP.RESPONSE_KO;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            this.getBdbam().stop();
            System.exit(1);
        }
        return null;
    }
}