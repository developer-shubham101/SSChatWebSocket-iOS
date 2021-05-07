package `in`.newdevpoint.ssnodejschat.stickyheader.stickyData

import `in`.newdevpoint.ssnodejschat.model.StickyMainData
import androidx.annotation.LayoutRes

interface HeaderData : StickyMainData {
    @LayoutRes
    fun getHeaderLayout(): Int
}