package com.luax.esp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(10, 12, 13)
    private val panel = Color.rgb(20, 24, 23)
    private val green = Color.rgb(74, 255, 54)
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("luax", MODE_PRIVATE)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 22, 18, 18)
            setBackgroundColor(bg)
        }
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val title = TextView(this).apply {
            text = "LUAX  ESP"; textSize = 23f; setTextColor(Color.WHITE)
            typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
        }
        header.addView(title, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(TextView(this).apply {
            text = "V3.0  •  DEMO"; textSize = 11f; setTextColor(green)
        })
        root.addView(header)
        root.addView(TextView(this).apply {
            text = "PAINEL FLUTUANTE  /  VISUAL MOD MENU"
            textSize = 10f; setTextColor(green); setPadding(0, 5, 0, 18)
        })
        val cover = ImageView(this).apply {
            setImageResource(R.drawable.luax_cover)
            scaleType = ImageView.ScaleType.CENTER_CROP
            adjustViewBounds = true
            setBackgroundColor(panel)
        }
        root.addView(cover, LinearLayout.LayoutParams(-1, 150).apply { bottomMargin = 12 })

        val gameCard = card()
        gameCard.addView(sectionTitle("JOGO / FONTE DE DADOS"))
        val gameInfo = TextView(this).apply {
            text = "Modo demonstração — alvos fictícios"
            setTextColor(Color.LTGRAY); textSize = 13f; setPadding(0, 12, 0, 10)
        }
        gameCard.addView(gameInfo)
        val gameButton = actionButton("ABRIR DEMONSTRAÇÃO")
        gameCard.addView(gameButton)
        root.addView(gameCard)

        val espCard = card()
        espCard.addView(sectionTitle("ESP  •  PLAYERS"))
        espCard.addView(toggleRow("Box em cantos", "box", true, prefs.getBoolean("box", true), prefs))
        espCard.addView(toggleRow("Skeleton", "skeleton", true, prefs.getBoolean("skeleton", true), prefs))
        espCard.addView(toggleRow("Distância", "distance", true, prefs.getBoolean("distance", true), prefs))
        espCard.addView(toggleRow("Box preenchida", "filled", false, prefs.getBoolean("filled", false), prefs))
        root.addView(espCard)

        val general = card()
        general.addView(sectionTitle("AJUSTES"))
        general.addView(label("Espessura da box"))
        general.addView(seek("boxWidth", 1, 5, prefs.getInt("boxWidth", 2), prefs))
        general.addView(label("Espessura da skeleton"))
        general.addView(seek("skWidth", 1, 5, prefs.getInt("skWidth", 2), prefs))
        general.addView(label("Distância máxima (metros)"))
        general.addView(seek("maxDistance", 10, 300, prefs.getInt("maxDistance", 300), prefs))
        root.addView(general)

        val controls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val start = actionButton("INICIAR ESP")
        val stop = actionButton("PARAR")
        controls.addView(start, LinearLayout.LayoutParams(0, 48, 1f).apply { rightMargin = 6 })
        controls.addView(stop, LinearLayout.LayoutParams(0, 48, 1f).apply { leftMargin = 6 })
        root.addView(controls)
        status = TextView(this).apply {
            text = "● PRONTO  |  Aguardando permissão de sobreposição"
            setTextColor(green); textSize = 11f; setPadding(2, 14, 2, 4)
        }
        root.addView(status)
        root.addView(TextView(this).apply {
            text = "A posição real só chega por telemetria enviada pelo seu próprio jogo ou por uma fonte autorizada. A VPN local não revela coordenadas internas de jogos de terceiros."
            textSize = 11f; setTextColor(Color.GRAY); setPadding(2, 12, 2, 0)
        })

        start.setOnClickListener {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                status.text = "● PERMISSÃO NECESSÁRIA  |  Autorize sobreposição e toque em INICIAR ESP"
            } else {
                startService(Intent(this, OverlayService::class.java))
                status.text = "● ESP ATIVA  |  Exibindo dados de demonstração"
            }
        }
        stop.setOnClickListener {
            stopService(Intent(this, OverlayService::class.java))
            status.text = "● PARADA"
        }
        gameButton.setOnClickListener {
            Toast.makeText(this, "Demo local selecionada. Integre a telemetria no seu próprio jogo.", Toast.LENGTH_LONG).show()
        }
        setContentView(ScrollView(this).apply { setBackgroundColor(bg); addView(root) })
    }

    private fun card() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(15, 12, 15, 14)
        setBackgroundColor(panel)
        val lp = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 12 }
        // margin is applied by caller through root.addView below
        tag = lp
    }

    private fun sectionTitle(s: String) = TextView(this).apply {
        text = s; textSize = 12f; setTextColor(green)
        typeface = Typeface.DEFAULT_BOLD
    }
    private fun label(s: String) = TextView(this).apply {
        text = s; textSize = 12f; setTextColor(Color.LTGRAY); setPadding(0, 12, 0, 2)
    }
    private fun actionButton(s: String) = Button(this).apply {
        text = s; textSize = 11f; setTextColor(Color.BLACK)
        setBackgroundColor(green); isAllCaps = false
    }
    private fun toggleRow(title: String, key: String, enabled: Boolean, initial: Boolean,
                          prefs: android.content.SharedPreferences): View {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        row.addView(TextView(this).apply { text = title; textSize = 13f; setTextColor(Color.WHITE) },
            LinearLayout.LayoutParams(0, 46, 1f))
        val check = Switch(this).apply {
            isChecked = initial; isEnabled = enabled; buttonTintList = null
            setOnCheckedChangeListener { _, value -> prefs.edit().putBoolean(key, value).apply() }
        }
        row.addView(check)
        return row
    }
    private fun seek(key: String, min: Int, max: Int, initial: Int, prefs: android.content.SharedPreferences): View {
        val bar = SeekBar(this).apply {
            this.max = max - min; progress = (initial - min).coerceIn(0, this.max)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, p: Int, fromUser: Boolean) {
                    if (fromUser) prefs.edit().putInt(key, p + min).apply()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        return bar
    }
}
