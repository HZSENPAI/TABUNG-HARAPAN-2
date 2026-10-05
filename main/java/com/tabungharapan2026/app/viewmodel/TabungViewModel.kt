package com.tabungharapan2026.app.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tabungharapan2026.app.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TabungViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val allTabung = database.tabungDao().getAllTabung().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val maybankList = database.ccDao().getCCByProvider("maybank").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allianceList = database.ccDao().getCCByProvider("alliance").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val allTransaksi = database.transaksiDao().getAllTransaksi().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tabungSavedTotal = database.tabungDao().getActiveTotalSaved().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val maybankSavedTotal = database.ccDao().getActiveTotalSavedByProvider("maybank").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val allianceSavedTotal = database.ccDao().getActiveTotalSavedByProvider("alliance").stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalBakiTabung = combine(tabungSavedTotal, maybankSavedTotal, allianceSavedTotal) { tabung, mb, al ->
        tabung + mb + al
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}
