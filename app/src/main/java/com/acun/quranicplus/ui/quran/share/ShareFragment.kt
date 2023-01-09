package com.acun.quranicplus.ui.quran.share

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.databinding.FragmentShareBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt

class ShareFragment : Fragment() {

    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!

    private val args: ShareFragmentArgs by navArgs()
    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setSystemUIVisibility(false)
        binding.imageButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.textViewTitle.text = buildString {
            append("QS. ")
            append(if (args.surah?.name?.transliteration?.en == null) args.verse.surahName else args.surah?.name?.transliteration?.en)
            append(": ")
            append(args.verse.number.inSurah)
        }

        binding.textViewContent.text = args.verse.text.arab
        binding.textViewContentTranslation.text = args.verse.translation.en

        binding.cardContainer.setOnClickListener {
            val startAnimation = ScaleAnimation(
                1f,
                .95f,
                1f,
                .95f,
                Animation.RELATIVE_TO_SELF,
                .5f,
                Animation.RELATIVE_TO_SELF,
                .5f
            )
            startAnimation.duration = 200
            startAnimation.fillAfter = true
            binding.cardContainer.startAnimation(startAnimation)

            val endAnimation = ScaleAnimation(
                .95f,
                1f,
                .95f,
                1f,
                Animation.RELATIVE_TO_SELF,
                .5f,
                Animation.RELATIVE_TO_SELF,
                .5f
            )
            endAnimation.duration = 200
            endAnimation.fillAfter = true
            binding.cardContainer.startAnimation(endAnimation)

            generateShareColor()
        }

        binding.buttonShare.setOnClickListener {
            val bitmap: Bitmap = binding.cardContainer.apply { radius = 0f }.drawToBitmap()
            file = File.createTempFile("share", ".jpg", requireActivity().cacheDir)

            val outputStream: OutputStream
            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fileUri = file?.let {
                FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    it
                )
            }

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, fileUri)
            }
            startActivity(Intent.createChooser(shareIntent, "Share"))
        }
    }

    private fun generateShareColor() {
        val colorArray = resources.getIntArray(R.array.share_background)
        val lastColor =
            colorArray.firstOrNull { it == binding.cardContainer.cardBackgroundColor.defaultColor }
        if (lastColor != null) {
            val lastIndex = colorArray.indexOf(lastColor)
            val index = if (lastIndex == colorArray.lastIndex) 0 else lastIndex + 1
            val currentColor = colorArray[index]
            binding.cardContainer.setCardBackgroundColor(currentColor)
            if (currentColor == ContextCompat.getColor(requireContext(), R.color.black)) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_black
                    )
                )
                requireActivity().window.statusBarColor =
                    ContextCompat.getColor(requireContext(), R.color.text_black)
            } else {
                binding.root.setBackgroundColor(currentColor)
                requireActivity().window.statusBarColor = currentColor
            }

            val inverseColor = getInverseBWColor(currentColor)
            binding.imageButtonBack.imageTintList = ColorStateList.valueOf(inverseColor)
            binding.textViewShareTitle.setTextColor(inverseColor)
            binding.textViewTitle.setTextColor(inverseColor)
            binding.textViewContent.setTextColor(inverseColor)
            binding.textViewContentTranslation.setTextColor(inverseColor)
            binding.textViewMore.setTextColor(inverseColor)
            binding.buttonShare.backgroundTintList = ColorStateList.valueOf(inverseColor)
            binding.buttonShare.setTextColor(getInverseBWColor(inverseColor))
            binding.textViewAppName.setTextColor(inverseColor)
            binding.imageViewAppIcon.imageTintList = ColorStateList.valueOf(inverseColor)
        }
    }

    private fun setSystemUIVisibility(isVisible: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.let {
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                if (isVisible) {
                    it.show(WindowInsets.Type.statusBars())
                } else {
                    it.hide(WindowInsets.Type.statusBars())
                }
            }
        }
    }

    private fun getInverseBWColor(color: Int): Int {
        val red = Color.red(color)
        val blue = Color.blue(color)
        val green = Color.green(color)

        val luminance = (red * 0.299) + (green * 0.7152) + (blue * 0.0722)
        val colorRes = if (luminance < 140) R.color.white else R.color.black
        return ContextCompat.getColor(requireContext(), colorRes)
    }

    override fun onResume() {
        super.onResume()
        binding.cardContainer.radius =
            (12 * Resources.getSystem().displayMetrics.density).roundToInt().toFloat()
        file?.delete()
    }

    override fun onDestroy() {
        super.onDestroy()
//        setSystemUIVisibility(true)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }
}