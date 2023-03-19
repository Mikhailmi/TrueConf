package ru.trueconf

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import ru.trueconf.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), OnTouchListener {
    private var binding: ActivityMainBinding? = null
    private var upMove = false

    private var scope = CoroutineScope(Job() + Dispatchers.Main)
    private var job: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        view.setOnTouchListener(this)
        setContentView(view)
        binding!!.textView.setOnClickListener {
            job?.cancel()
        }
    }


    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility", "SetTextI18n")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        job?.cancel()
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        val layoutParams = binding!!.textView.layoutParams as FrameLayout.LayoutParams
        layoutParams.leftMargin = x - 100
        layoutParams.topMargin = y - 250
        layoutParams.rightMargin = 0
        layoutParams.bottomMargin = 0
        binding!!.textView.layoutParams = layoutParams
        if (Locale.getDefault().country == "RU") binding!!.textView.setTextColor(R.color.purple_700)
        else if (Locale.getDefault().country == "EN") binding!!.textView.setTextColor(
            R.color.red
        )
        binding!!.container.invalidate()
        upMove = false
        job = scope.launch {
            delay(5000)
            while (true) {
                if (upMove) {
                    binding!!.textView.bottom -= 1
                    binding!!.textView.top -= 1
                    if (0 >= binding!!.textView.top)
                        upMove = false
                } else {
                    binding!!.textView.bottom += 1
                    binding!!.textView.top += 1
                    if (binding!!.textView.bottom >= binding!!.container.height)
                        upMove = true
                }
                delay(1)
            }
        }
        return true
    }
}