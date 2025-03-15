/*
 *  Copyright (c) 2025 Anthony Gallop <agallopdev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ransomsensei.activity_main.util

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable object CardSetsScreen : Destination
    @Serializable class AddEditCardSetScreen(val cardSetId: Int? = null): Destination
    @Serializable class CardSetDetailsScreen(val cardSetId: Int): Destination
    @Serializable class AddEditCardScreen(val cardSetId: Int, val cardId: Int? = null): Destination
}