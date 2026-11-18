package com.demo.catalog.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val rating: Double,
    val brand: String?,
    val thumbnail: String,
)

fun Product.toEntity() = ProductEntity(id, title, description, price, category, rating, brand, thumbnail)
fun ProductEntity.toProduct() = Product(id, title, description, price, category, rating, brand, thumbnail)

@Dao
abstract class ProductDao {
    @Query("SELECT * FROM products ORDER BY id")
    abstract suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id")
    abstract suspend fun getById(id: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(items: List<ProductEntity>)

    @Query("DELETE FROM products")
    abstract suspend fun deleteAll()

    /** Заменяет весь кэш свежим списком в одной транзакции. */
    @Transaction
    open suspend fun replaceAll(items: List<ProductEntity>) {
        deleteAll()
        insertAll(items)
    }
}

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
