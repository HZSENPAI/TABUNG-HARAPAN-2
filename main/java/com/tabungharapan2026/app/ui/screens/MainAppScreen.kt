package com.tabungharapan2026.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tabungharapan2026.app.ui.theme.*
import com.tabungharapan2026.app.viewmodel.TabungViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: TabungViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val currencyFormat = remember { DecimalFormat("#,##0.00") }

    val totalBaki by viewModel.totalBakiTabung.collectAsState()
    val tabungSaved by viewModel.tabungSavedTotal.collectAsState()
    val maybankSaved by viewModel.maybankSavedTotal.collectAsState()
    val allianceSaved by viewModel.allianceSavedTotal.collectAsState()

    val tabungList by viewModel.allTabung.collectAsState()
    val maybankList by viewModel.maybankList.collectAsState()
    val allianceList by viewModel.allianceList.collectAsState()
    val transaksiList by viewModel.allTransaksi.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TABUNG HARAPAN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = RoseTextMain
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoseBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Ringkasan") },
                    label = { Text("Ringkasan") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Transaksi") },
                    label = { Text("Transaksi") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Tabung") },
                    label = { Text("Tabung") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.CreditCard, contentDescription = "CC") },
                    label = { Text("CC") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(RoseBackground)
        ) {
            when (selectedTab) {
                0 -> DashboardContent(totalBaki, tabungSaved, maybankSaved, allianceSaved, currencyFormat)
                1 -> TransaksiContent(transaksiList, currencyFormat)
                2 -> TabungContent(tabungList, currencyFormat)
                3 -> CCContent(maybankList, allianceList, currencyFormat)
            }
        }
    }
}

@Composable
fun DashboardContent(totalBaki: Double, tabungSaved: Double, maybankSaved: Double, allianceSaved: Double, format: DecimalFormat) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = RosePrimary)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BAKI TABUNG", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("RM " + format.format(totalBaki), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tabung", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            Text("RM " + format.format(tabungSaved), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CC Maybank", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            Text("RM " + format.format(maybankSaved), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CC Alliance", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                            Text("RM " + format.format(allianceSaved), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TabungContent(tabungList: List<com.tabungharapan2026.app.data.TabungEntity>, format: DecimalFormat) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(tabungList) { item ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.nama, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Jumlah Disimpan: RM " + format.format(item.jumlahDisimpan), color = RosePrimary, fontWeight = FontWeight.Bold)
                    Text("Sasaran: RM " + format.format(item.sasaran), fontSize = 12.sp, color = RoseTextMuted)
                }
            }
        }
    }
}

@Composable
fun CCContent(maybankList: List<com.tabungharapan2026.app.data.CCEntity>, allianceList: List<com.tabungharapan2026.app.data.CCEntity>, format: DecimalFormat) {
    var selectedCC by remember { mutableIntStateOf(0) }
    val currentList = if (selectedCC == 0) maybankList else allianceList
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TabRow(selectedTabIndex = selectedCC, containerColor = Color.White) {
            Tab(selected = selectedCC == 0, onClick = { selectedCC = 0 }, text = { Text("CC Maybank") })
            Tab(selected = selectedCC == 1, onClick = { selectedCC = 1 }, text = { Text("CC Alliance") })
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(currentList) { item ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(item.perkara, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Jumlah Disimpan: RM " + format.format(item.jumlahDisimpan), color = RosePrimary, fontWeight = FontWeight.Bold)
                        Text("Perlu Disimpan: RM " + format.format(item.perluDisimpan), fontSize = 12.sp, color = RoseTextMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun TransaksiContent(transaksiList: List<com.tabungharapan2026.app.data.TransaksiEntity>, format: DecimalFormat) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(transaksiList) { item ->
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(item.nama, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(item.tarikh + " • " + item.kategori.uppercase(), fontSize = 11.sp, color = RoseTextMuted)
                    }
                    Text((if (item.jenis == "simpan") "+" else "-") + " RM " + format.format(item.jumlah), color = if (item.jenis == "simpan") EmeraldGreen else RosePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
