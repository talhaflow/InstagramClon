package com.talhakara.instagram.ViewModel

import androidx.lifecycle.ViewModel
import com.talhakara.instagram.View.DmGenel

class DmViewModel : ViewModel() {
    // ViewModel içinde tutmak istediğiniz verileri burada tanımlayabilirsiniz.
    private val dmGenelList: List<DmGenel> = listOf(
        DmGenel("Kullanici1", "Merhaba, nasılsın?"),
        DmGenel("Kullanici2", "İyi, teşekkür ederim!"),
        // Diğer verileri burada ekleyin
    )

    // ViewModel'den verilere erişim yöntemleri
    fun getDmGenelList(): List<DmGenel> {
        return dmGenelList
    }
}
