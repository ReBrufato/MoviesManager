package com.example.moviesmanager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.moviesmanager.R
import com.example.moviesmanager.adapters.MovieAdapter
import com.example.moviesmanager.databinding.ActivityMainBinding
import com.example.moviesmanager.model.Constants.EXTRA_MOVIE
import com.example.moviesmanager.model.Constants.VIEW_MOVIE
import com.example.moviesmanager.model.Movie

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val movieslList: MutableList<Movie> = mutableListOf()

    private lateinit var movieAdapter: MovieAdapter

    private lateinit var marl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        supportActionBar?.subtitle = "Lista de Filmes"

        populaListaMovies()

        movieAdapter = MovieAdapter(this, movieslList)
        amb.moviesLv.adapter = movieAdapter

        marl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ){result ->
            if(result.resultCode == RESULT_OK){
                val movie = result.data?.getParcelableExtra<Movie>(EXTRA_MOVIE)

                movie?.let {_movie->
                    val position = movieslList.indexOfFirst { it.id == _movie.id }

                    //edição
                    if(position != -1) {
                        movieslList[position] = _movie
                    }
                    //adição
                    else{
                        movieslList.add(_movie)
                    }
                    movieAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(amb.moviesLv)

        amb.moviesLv.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
            val movie = movieslList[position]
            val integralIntent = Intent(this@MainActivity, MovieActivity::class.java)
            integralIntent.putExtra(EXTRA_MOVIE, movie)
            integralIntent.putExtra(VIEW_MOVIE, true)
            startActivity(integralIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.addMovie -> {
                val intentIntegralActivity = Intent(this,MovieActivity::class.java)
                marl.launch(intentIntegralActivity)
                true
            }
            else -> {false}
        }
    }

    private fun populaListaMovies(){
        movieslList.add(
            Movie(1, "Assalto ao banco central", "2011", null, "Fox filmes", 101, false, null, "Ação")
        )
        movieslList.add(
            Movie(2, "O Pequenino", "2006", null, "Columbia Pictures", 98, false, null, "Comédia")
        )
        movieslList.add(
            Movie(1, "Hannibal", "2001", null, "Universal Studios", 131, false, null, "Crime")
        )
    }
}