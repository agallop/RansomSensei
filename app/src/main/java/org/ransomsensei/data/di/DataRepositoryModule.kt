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
package org.ransomsensei.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.ransomsensei.data.RansomSenseiDataRepository
import org.ransomsensei.data.RansomSenseiDataRepositoryImpl
import org.ransomsensei.data.RansomSenseiDataStoreManager
import org.ransomsensei.data.RansomSenseiDatabase

val dataRepositoryModule = module {
    single { androidContext().packageManager }
    single<RansomSenseiDataRepository> {
        RansomSenseiDataRepositoryImpl(
            _packageManager = get(),
            database = RansomSenseiDatabase.createInstance(androidContext()),
            _dataStoreManager = RansomSenseiDataStoreManager(androidContext())
        )
    }
}