package com.apptolast.lifetimejournal.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apptolast.lifetimejournal.DATABASE_NAME

//fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
//    val appContext = context.applicationContext
//    val dbFile = context.getDatabasePath(DATABASE_NAME)
//    return Room.databaseBuilder<AppDatabase>(
//        context = appContext,
//        name = dbFile.absolutePath,
//    )
//}


//@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
//actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
//    override fun initialize(): AppDatabase {
//        TODO("Not yet implemented")
////        val appContext = context.applicationContext
//        val dbFile = context.getDatabasePath(DATABASE_NAME)
//        return Room.databaseBuilder<AppDatabase>(
//            context = context,
//            name = dbFile.absolutePath,
//        )
//    }
//}

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}

//actual object AppDatabaseConstructor : AppDatabaseConstructor {
//    override fun initialize(): AppDatabase {
//        // En Android, la inicialización requiere un Context.
//        // Lanzamos una excepción si se llama a esta versión sin contexto,
//        // forzando el uso de la función con Context.
//        throw IllegalStateException("Call `initialize(context: Context)` instead on Android.")
//    }
//
//    // Función específica para Android que recibe el Context
//    fun initialize(context: Context): AppDatabase {
//        val appContext = context.applicationContext
//        val dbFile = context.getDatabasePath(DATABASE_NAME)
//
//        // Usamos Room.databaseBuilder con el Context de Android
//        val builder = Room.databaseBuilder(
//            context.applicationContext, // Usamos el applicationContext para evitar leaks
//            AppDatabase::class.java, // La clase de la base de datos
//            dbFile.absolutePath, // El nombre del archivo de la base de datos
//        )
//            .addMigrations() // Si tienes migraciones, agrégalas aquí
//            .fallbackToDestructiveMigrationOnDowngrade(true) // Opcional: reconstruye si baja la versión
//
//        // Pasamos el builder a la factory definida en commonMain
//        return AppDatabase.DatabaseFactory.create(builder)
//    }
//}
