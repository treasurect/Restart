package com.treasure.restart.func.main.market

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarketViewModel : ViewModel() {
    val title = MutableLiveData("市集")
    val emptyHint = MutableLiveData("市集功能建设中，敬请期待")
}
