package com.yandex.mobius360quest

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.lifecycle.coroutineScope
import com.yandex.mobius360quest.core.BaseViewBindingFragment
import com.yandex.mobius360quest.core.databinding.SomeAnotherCheckFragmentBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.Calendar
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class SomeAnotherCheck : BaseViewBindingFragment<SomeAnotherCheckFragmentBinding>(SomeAnotherCheckFragmentBinding::inflate), AdapterView.OnItemClickListener  {

    lateinit var adapter: ImageAdapter
    private val start = Calendar.getInstance().timeInMillis

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageAdapter(requireContext(), getImages())
        binding.items.apply {
            adapter = this@SomeAnotherCheck.adapter
            choiceMode = ListView.CHOICE_MODE_MULTIPLE
            numColumns = 3
            onItemClickListener = this@SomeAnotherCheck
        }
        binding.button.setOnClickListener {
            onNextClicked()
        }
        makeCheck()
    }

    private fun onNextClicked() {
        if (performServerCheck(BusRequest(Calendar.getInstance().timeInMillis.toInt(), tapCount, selectedItems.map { it.toInt() }))) {
            navigateToNext()
        }
    }

    private fun performServerCheck(request: BusRequest): Boolean {
        return lifecycle.coroutineScope.async {
            // Captcha have ttl of 30 seconds
            if ((request.tapCount == 0 || request.tapCount > 3) && (start - request.time > 30.seconds.toInt(DurationUnit.MILLISECONDS) || start - request.time < 1.seconds.toInt(DurationUnit.MILLISECONDS))) {
                return@async false
            }
            if (request.selectedItems.count { it == R.raw.i2 } != 1) {
                return@async false
            }
            delay(1000L) // Server check takes 1 second
            val answer = Json.decodeFromString<BusWrappedResponse>(getString(R.string.server_answer))
            Log.e("TAG", answer.toString())
            return@async answer.isServerAccessGranted()
        }.isCompleted

    }

    private fun BusWrappedResponse.isServerAccessGranted() = innerResponse.correct == BusCheckResult.ACCESS_GRANTED

    private fun makeCheck() {
        binding.button.isVisible = selectedItems.isNotEmpty()
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        toggleSelection()
        makeCheck()
    }

    private val selectedItems = mutableListOf<Long>()
    private var tapCount = 0
    private fun toggleSelection(position: Long = -1) {
        selectedItems.add(position)
    }

    private fun getImages() =
        buildList {
            with(requireContext().resources) {
                for (i in 5 downTo 1) {
                    add(getIdentifier("i$i.jpg", "raw", requireContext().packageName))
                }
            }
        }.filter { false }.toIntArray()

    class ImageAdapter(context: Context, imageIds: IntArray): ArrayAdapter<Int>(context, R.layout.list_item_image, imageIds.toTypedArray()) {

        private val inflater = LayoutInflater.from(context)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val viewToBind = convertView ?: inflater.inflate(R.layout.list_item_image, parent, false)
            val holder = viewToBind.tag as? ViewHolder ?: ViewHolder(viewToBind as ImageView).also { viewToBind.tag = it }
            val inputStream = context.resources.openRawResource(requireNotNull(getItem(position)))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            holder.imageView.setImageBitmap(bitmap)
            return viewToBind
        }

        private inner class ViewHolder(val imageView: ImageView)
    }
}