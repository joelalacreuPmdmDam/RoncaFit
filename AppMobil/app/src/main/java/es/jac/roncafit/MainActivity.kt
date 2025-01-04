package es.jac.roncafit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationView
import es.jac.roncafit.databinding.ActivityMainBinding
import es.jac.roncafit.fragments.CalculadoraRMFragment
import es.jac.roncafit.fragments.DetalleActividadFragment
import es.jac.roncafit.fragments.InicioFragment
import es.jac.roncafit.fragments.ListaEjerciciosFragment
import es.jac.roncafit.fragments.ListaRutinasFragment
import es.jac.roncafit.fragments.QRAuthFragment
import es.jac.roncafit.fragments.RegistrosSeriesFragment
import es.jac.roncafit.models.actividades.ActividadKot
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.RutinaResponse

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,InicioFragment.InicioFragmentListener,
    ListaEjerciciosFragment.EjercicioFragmentListener, ListaRutinasFragment.ListaRutinasFragmentListener {

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

            R.id.rm_calculator_nav_option -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<CalculadoraRMFragment>(R.id.fragmentContainerView_main)
                    addToBackStack(null)
                }
                return true
            }

            R.id.chat_nav_option -> {
                Toast.makeText(this,"Has pulsado chat",Toast.LENGTH_SHORT).show()
                return true
            }


            R.id.settings_nav_option -> {
                Toast.makeText(this,"Has pulsado ajustes",Toast.LENGTH_SHORT).show()
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
            if (currentFragment !is InicioFragment && currentFragment !is RegistrosSeriesFragment && currentFragment !is ListaEjerciciosFragment) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<InicioFragment>(R.id.fragmentContainerView_main)
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    //NavigationDrawer config end

    override fun onActividadClicked(actividad: ActividadResponse) {
        val fragment = DetalleActividadFragment.newInstance(actividad)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onEjercicioClicked(ejercicio: EjerciciosResponse, idRutina: Int) {
        val fragment = RegistrosSeriesFragment.newInstance(ejercicio, idRutina)
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
}