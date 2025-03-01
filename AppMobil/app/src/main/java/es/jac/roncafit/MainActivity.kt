package es.jac.roncafit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import es.jac.roncafit.databinding.ActivityMainBinding
import es.jac.roncafit.fragments.AlertasFragment
import es.jac.roncafit.fragments.CalculadoraRMFragment
import es.jac.roncafit.fragments.ChatFragment
import es.jac.roncafit.fragments.ClientesChatFragment
import es.jac.roncafit.fragments.DetalleActividadFragment
import es.jac.roncafit.fragments.DetalleEjercicioFragment
import es.jac.roncafit.fragments.InfoPersonalFragment
import es.jac.roncafit.fragments.InicioFragment
import es.jac.roncafit.fragments.ListaEjerciciosFragment
import es.jac.roncafit.fragments.ListaRutinasFragment
import es.jac.roncafit.fragments.QRAuthFragment
import es.jac.roncafit.fragments.RegistrosSeriesFragment
import es.jac.roncafit.fragments.ReservasFragment
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.actividades.ActividadKot
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.RutinaResponse
import es.jac.roncafit.models.usuarios.ClienteResponse
import es.jac.roncafit.services.usuarios.ClientesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,InicioFragment.InicioFragmentListener,
    ListaEjerciciosFragment.EjercicioFragmentListener, ListaRutinasFragment.ListaRutinasFragmentListener, ClientesChatFragment.ClientesChatFragmentListener,
    ChatFragment.ChatFragmenListener, ReservasFragment.ReservasFragmentListener{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
        setUpNavigationDrawer()
    }

    override fun onResume() {
        super.onResume()
        // Información del usuario
        val sharedPreferences = getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("auth_nombreUsuario", null)
        val userId = sharedPreferences.getInt("auth_idCliente", -1)
        //Acceder al txt del header que contiene el nombre
        val headerNavigationView = binding.navigationView.getHeaderView(0)
        val txtNombreHeader = headerNavigationView.findViewById<TextView>(R.id.nav_myName)
        txtNombreHeader.text = userName.toString()
        //Acceder al txt del header que contiene el idUsuario
        val txtEmailHeader = headerNavigationView.findViewById<TextView>(R.id.nav_myId)
        txtEmailHeader.text = userId.toString()
        obtenerPerfil(userId);
    }

    private fun obtenerPerfil(idCliente: Int) {
        val sharedPreferences = this.getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(this, "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(ClientesService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val perfilCliente = apiService.obtenerPerfil("Bearer $token", idCliente)
                withContext(Dispatchers.Main) {
                    if (!perfilCliente.imagen.isNullOrBlank()){
                        val headerNavigationView = binding.navigationView.getHeaderView(0)
                        val imgUsuario = headerNavigationView.findViewById<ImageView>(R.id.image_header)
                        val decodedString = Base64.decode(perfilCliente.imagen, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        imgUsuario.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error en la solicitud, contacte con el administrador", Toast.LENGTH_SHORT).show()
                    Log.d("Error MainActivity",e.message.toString())
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    //Metodo onCLick de la toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.user_qr_info -> {
                val intent = Intent(this,QRActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    //NavigationDrawer config start
    private fun setUpNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.mainToolbar,
            R.string.chat,
            R.string.iniciarSesion
        )
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.colorBackground, theme)
        binding.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView?.setNavigationItemSelectedListener(this)

        //Check the first option
        binding.navigationView.setCheckedItem(R.id.home_nav_option)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout?.closeDrawer(GravityCompat.START)

        return when(item.itemId) {
            R.id.home_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<InicioFragment>(R.id.fragmentContainerView_main)
                }
                return true
            }

            R.id.rutinas_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ListaRutinasFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }

            R.id.avisos_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AlertasFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }

            R.id.mis_reservas_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ReservasFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }

            R.id.rm_calculator_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<CalculadoraRMFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }

            R.id.chat_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ClientesChatFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }


            R.id.settings_nav_option -> {
                val sharedPreferences = getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("auth_idCliente", -1)
                val fragment = InfoPersonalFragment.newInstance(userId)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView_main, fragment)
                    .addToBackStack(null)
                    .commit()
                return true
            }
            else -> {
                return false
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView_main)
            if (currentFragment !is InicioFragment && currentFragment !is RegistrosSeriesFragment
                && currentFragment !is ListaEjerciciosFragment && currentFragment !is ChatFragment && currentFragment !is DetalleEjercicioFragment) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<InicioFragment>(R.id.fragmentContainerView_main)
                }
                binding.navigationView?.setCheckedItem(R.id.home_nav_option)
            } else {
                super.onBackPressed()
            }
        }
    }

    //NavigationDrawer config end

    override fun onActividadClickedInicio(actividad: ActividadResponse) {
        val fragment = DetalleActividadFragment.newInstance(actividad,1)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onActividadClickedReservas(actividad: ActividadResponse) {
        val fragment = DetalleActividadFragment.newInstance(actividad,0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }


    //Métodos Fragments lista rutinas y lista ejercicios (RUTINAS)
    override fun onEjercicioClicked(ejercicio: EjerciciosResponse, idRutina: Int) {
        val fragment = RegistrosSeriesFragment.newInstance(ejercicio, idRutina)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onInfoClicked(ejercicio: EjerciciosResponse) {
        val fragment = DetalleEjercicioFragment.newInstance(ejercicio)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onRutinaClicked(rutina: RutinaResponse) {
        val fragment = ListaEjerciciosFragment.newInstance(rutina)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }


    //Métodos Fragment lista clientes (CHAT)
    override fun onClienteClicked(cliente: ClienteResponse) {
        val fragment = ChatFragment.newInstance(cliente)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onChatGlobalBtnPressed() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ChatFragment>(R.id.fragmentContainerView_main)
            addToBackStack(null)
        }
    }

    override fun onToolbarClicked(idCliente: Int) {
        val fragment = InfoPersonalFragment.newInstance(idCliente)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }
}