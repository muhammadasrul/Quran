package com.acun.quranicplus.ui.quran.share

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.acun.quranicplus.R
import com.acun.quranicplus.databinding.FragmentShareBinding
import com.acun.quranicplus.ui.compose.ShareScreen
import java.io.File

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

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val colorArray = arrayOf(R.color.primary_blue_extra_light, R.color.primary_blue ,R.color.black ,R.color.white)
                val primaryColor = remember { mutableStateOf(R.color.white) }
                val secondaryColor = remember { mutableStateOf(R.color.black) }

                ShareScreen(
                    verse = args.verse,
                    surah = args.surah,
                    primaryColor = primaryColor.value,
                    secondaryColor = secondaryColor.value,
                    onBackPressed = {
                        findNavController().navigateUp()
                    },
                    onCardClicked = {
                        val lastIndex = colorArray.indexOfFirst { it == primaryColor.value }
                        val index = if (lastIndex == colorArray.lastIndex) 0 else lastIndex + 1
                        primaryColor.value = colorArray[index]
                        secondaryColor.value = getInverseBWColor(colorArray[index])
                    },
                    onShareClicked = {

                    }
                )
            }
        }

//        binding.buttonShare.setOnClickListener {
//            val bitmap: Bitmap = binding.cardContainer.apply { radius = 0f }.drawToBitmap()
//            file = File.createTempFile("share", ".jpg", requireActivity().cacheDir)
//
//            val outputStream: OutputStream
//            try {
//                outputStream = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                outputStream.flush()
//                outputStream.close()
//            } catch (e: Exception) {
//                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val fileUri = file?.let {
//                FileProvider.getUriForFile(
//                    requireContext(),
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    it
//                )
//            }
//
//            val shareIntent = Intent(Intent.ACTION_SEND).apply {
//                type = "image/*"
//                putExtra(Intent.EXTRA_STREAM, fileUri)
//            }
//            startActivity(Intent.createChooser(shareIntent, "Share"))
//        }
    }

    private fun getInverseBWColor(color: Int): Int {
        val red = Color.red(ContextCompat.getColor(requireContext(), color))
        val blue = Color.blue(ContextCompat.getColor(requireContext(), color))
        val green = Color.green(ContextCompat.getColor(requireContext(), color))

        val luminance = (red * 0.299) + (green * 0.7152) + (blue * 0.0722)
        return if (luminance < 140) R.color.white else R.color.black
    }

    override fun onResume() {
        super.onResume()
//        binding.cardContainer.radius = (12 * Resources.getSystem().displayMetrics.density).roundToInt().toFloat()
        file?.delete()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }
}