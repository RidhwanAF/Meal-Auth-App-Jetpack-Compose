package com.raf.authentikasilogin.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.raf.authentikasilogin.R
import com.raf.authentikasilogin.network.response.DataItem
import com.raf.authentikasilogin.ui.setting.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val listUser = mainViewModel.listUserPager.collectAsLazyPagingItems()
    val snackBarHostState = remember { SnackbarHostState() }
    var openSettingDialog by remember {
        mutableStateOf(false)
    }

    if (openSettingDialog) {
        Dialog(
            onDismissRequest = {
                openSettingDialog = false
            }
        ) {
            SettingsScreen()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.list_user))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                openSettingDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        }
        when (listUser.loadState.refresh) {
            is LoadState.Loading -> {

            }

            is LoadState.Error -> {
                val errorMessage = stringResource(R.string.failed_to_retrieve_data)
                LaunchedEffect(snackBarHostState) {
                    snackBarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )
                }
                Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
                    Modifier.padding(
                        paddingValues
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(listUser.itemCount) { index ->
                        UserItem(listUser[index])
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(dataItem: DataItem?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = dataItem?.avatar,
                placeholder = painterResource(R.drawable.ic_account_circle),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${dataItem?.firstName} ${dataItem?.lastName}",
            )
            Text(
                text = dataItem?.email ?: "",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}