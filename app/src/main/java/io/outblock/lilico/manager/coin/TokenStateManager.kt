package io.outblock.lilico.manager.coin

import com.google.gson.annotations.SerializedName
import io.outblock.lilico.cache.tokenStateCache
import io.outblock.lilico.manager.flowjvm.cadenceCheckTokenEnabled
import io.outblock.lilico.manager.flowjvm.cadenceCheckTokenListEnabled
import io.outblock.lilico.utils.ioScope
import io.outblock.lilico.utils.logd
import io.outblock.lilico.utils.logw
import io.outblock.lilico.utils.uiScope
import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArrayList

object TokenStateManager {
    private val TAG = TokenStateManager::class.java.simpleName

    private val tokenStateList = CopyOnWriteArrayList<TokenState>()
    private val listeners = CopyOnWriteArrayList<WeakReference<TokenStateChangeListener>>()

    fun reload() {
        ioScope {
            tokenStateList.clear()
            tokenStateList.addAll(tokenStateCache().read()?.stateList ?: emptyList())
        }
    }

    fun fetchState() {
        ioScope { fetchStateSync() }
    }

    fun fetchStateSync() {
        val coinList = FlowCoinListManager.coinList()
        val isEnableList = cadenceCheckTokenListEnabled(FlowCoinListManager.coinList())
        if (coinList.size != isEnableList?.size) {
            logw(TAG, "fetch error")
            return
        }
        coinList.forEachIndexed { index, coin ->
            val isEnable = isEnableList[index]
            val oldState = tokenStateList.firstOrNull { it.symbol == coin.symbol }
            tokenStateList.remove(oldState)
            tokenStateList.add(TokenState(coin.symbol, coin.address(), isEnable))
            if (oldState?.isAdded != isEnable) {
                dispatchListeners(coin, isEnable)
            }
        }
        tokenStateCache().cache(TokenStateCache(tokenStateList.toList()))
    }

    fun fetchStateSingle(coin: FlowCoin, cache: Boolean = false) {
        val isEnable = cadenceCheckTokenEnabled(coin)
        if (isEnable != null) {
            val oldState = tokenStateList.firstOrNull { it.symbol == coin.symbol }
            tokenStateList.remove(oldState)
            tokenStateList.add(TokenState(coin.symbol, coin.address(), isEnable))
            if (oldState?.isAdded != isEnable) {
                dispatchListeners(coin, isEnable)
            }
        }
        if (cache) {
            tokenStateCache().cache(TokenStateCache(tokenStateList.toList()))
        }
    }

    fun isTokenAdded(tokenAddress: String) = tokenStateList.firstOrNull { it.address == tokenAddress }?.isAdded ?: false

    fun addListener(callback: TokenStateChangeListener) {
        uiScope { this.listeners.add(WeakReference(callback)) }
    }

    private fun dispatchListeners(coin: FlowCoin, isEnable: Boolean) {
        logd(TAG, "${coin.name} isEnable:$isEnable")
        uiScope {
            listeners.removeAll { it.get() == null }
            listeners.toList().forEach { it.get()?.onTokenStateChange(coin, isEnable) }
        }
    }
}

interface TokenStateChangeListener {
    fun onTokenStateChange(coin: FlowCoin, isEnable: Boolean)
}

class TokenStateCache(
    @SerializedName("stateList")
    val stateList: List<TokenState> = emptyList(),
)

class TokenState(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("isAdded")
    val isAdded: Boolean,
)