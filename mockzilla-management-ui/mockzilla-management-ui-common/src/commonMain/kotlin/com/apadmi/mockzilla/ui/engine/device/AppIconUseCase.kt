package com.apadmi.mockzilla.ui.engine.device

import com.apadmi.mockzilla.management.MockzillaManagement

interface AppIconUseCase {
    suspend fun getAppIcon(device: Device): Result<ByteArray?>
}

class AppIconUseCaseImpl(
    private val appIconService: MockzillaManagement.AppIconService
) : AppIconUseCase {
    override suspend fun getAppIcon(device: Device): Result<ByteArray?> =
        appIconService.fetchAppIcon(device)
}
