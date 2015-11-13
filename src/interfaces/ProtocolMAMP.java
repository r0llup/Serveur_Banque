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

package interfaces;

/**
 * Manage a {@link ProtocolMAMP}
 * @author Sh1fT
 */
public interface ProtocolMAMP {
    public Integer transferPogn(Double somme, String nomClient);
}