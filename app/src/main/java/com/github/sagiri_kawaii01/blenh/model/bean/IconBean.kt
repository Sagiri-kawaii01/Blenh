package com.github.sagiri_kawaii01.blenh.model.bean

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Elderly
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.WaterDrop
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.sagiri_kawaii01.blenh.base.BaseBean
import kotlinx.serialization.Serializable


const val ICON_TABLE_NAME = "Icon"

@Serializable
@Entity(
    tableName = ICON_TABLE_NAME
)
data class IconBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID_COLUMN)
    val id: Int = 0,
    @ColumnInfo(name = RES_ID_COLUMN)
    val resId: Int
): BaseBean {
    companion object {
        const val ID_COLUMN = "id"
        const val RES_ID_COLUMN = "resId"
        val IconList = listOf(
            Icons.Filled.Restaurant,
            Icons.Filled.ShoppingCart,
            Icons.Filled.DirectionsBus,
            Icons.Filled.Pets,
            Icons.Filled.WaterDrop,
            Icons.Filled.Vaccines,
            Icons.Filled.Elderly,
            Icons.Filled.SportsTennis,
        )
    }
}