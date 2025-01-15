package es.jac.roncafit.managers

import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.chat.MensajeFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class FirestoreChatManager {


    private val firestore by lazy { FirebaseFirestore.getInstance() }


    suspend fun addMensaje(msj: MensajeFlow): Boolean {
        return try {
            msj.createdData = Timestamp.now().toDate()
            firestore.collection(MENSAJE_COLLECTION).add(msj).await()
            true
        }catch(e: Exception){
            false
        }
    }

    suspend fun getMensajesFlow(idDestinatario: Int?): Flow<List<MensajeFlow>> = callbackFlow {
        var mensajeCollection: CollectionReference? = null
        try {
            mensajeCollection = FirebaseFirestore.getInstance()
                .collection(MENSAJE_COLLECTION)

            //.whereEqualTo("idDestinatario", idDestinatario)? // Cuando añado la clausula where no filtra bien :((((
            val subscription = mensajeCollection?.orderBy(CREATED_DATA, Query.Direction.ASCENDING)?.addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        val mensajes = mutableListOf<MensajeFlow>()
                        snapshot.forEach {
                            mensajes.add(
                                MensajeFlow(
                                    idRemitente = it.get(MENSAJE_REMITENTE).toString().toInt(),
                                    mensaje = it.get(MENSAJE_CONTENT).toString(),
                                    infoRemitente = it.get(MENSAJE_INFO_REMITENTE).toString(),
                                    createdData = it.getTimestamp(CREATED_DATA)?.toDate()
                                )
                            )
                        }
                        trySend(mensajes)
                    }
                }
            awaitClose { subscription?.remove() }
        } catch (e: Throwable) {
            close(e)
        }
    }


    suspend fun getNombresChat(): Flow<List<Cliente>> = callbackFlow{
        var nombresCollection: CollectionReference? = null
        try {
            nombresCollection = FirebaseFirestore.getInstance()
                .collection(NOMBRE_CHAT_COLLECTION)

            val subscription = nombresCollection?.addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val clientes = mutableListOf<Cliente>()
                    snapshot.forEach{
                        clientes.add(
                            Cliente(
                                idCliente = it.get(ID_USUARIO).toString().toInt(),
                                nombreCliente = it.get(NOMBRE_CHAT).toString(),
                                imagen = it.get(IMG_PERFIL).toString()
                            )
                        )
                    }
                    trySend(clientes)
                }
            }
            awaitClose { subscription?.remove() }
        } catch (e: Throwable) {
            close(e)
        }
    }


    suspend fun updateNombreChat(cliente: Cliente): Boolean {
        return try {
            val userRef = cliente.idCliente.let { firestore.collection(NOMBRE_CHAT_COLLECTION).document(it.toString()) }
            userRef?.set(cliente)?.await()
            true
        }catch(e: Exception){
            false
        }
    }



    companion object{
        //Variables colecciones
        const val MENSAJE_COLLECTION = "mensajes"
        const val NOMBRE_CHAT_COLLECTION = "clientes"

        //Variables atributos coleccion 'mensajes'
        const val MENSAJE_REMITENTE = "idRemitente"
        const val MENSAJE_CONTENT = "mensaje"
        const val MENSAJE_INFO_REMITENTE = "infoRemitente"
        const val CREATED_DATA = "createdData"

        //Variables atributos coleccion 'clientes'
        const val ID_USUARIO = "idCliente"
        const val NOMBRE_CHAT = "nombreCliente"
        const val IMG_PERFIL = "imagen"
    }
}