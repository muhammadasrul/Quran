package com.acun.quranicplus.ui.quran.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.acun.quranicplus.BuildConfig
import com.acun.quranicplus.R
import com.acun.quranicplus.databinding.FragmentShareBinding
import com.acun.quranicplus.ui.compose.ShareScreen
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ShareFragment : Fragment() {

    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!

    private val args: ShareFragmentArgs by navArgs()
    private var file: File? = null

    private val statusBarColor = MutableLiveData(R.color.white)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusBarColor.observe(viewLifecycleOwner) {
            requireActivity().window.statusBarColor = if (it == R.color.black) {
                ContextCompat.getColor(requireContext(), R.color.text_black)
            } else {
                ContextCompat.getColor(requireContext(), it)
            }
        }

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val colorArray = arrayOf(R.color.primary_blue_extra_light, R.color.primary_blue ,R.color.black ,R.color.white)
                var primaryColor by remember { mutableStateOf(R.color.white) }
                var secondaryColor by remember { mutableStateOf(R.color.black) }

                ShareScreen(
                    verse = args.verse,
                    surah = args.surah,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor,
                    onBackPressed = {
                        findNavController().navigateUp()
                    },
                    onCardClicked = {
                        val lastIndex = colorArray.indexOfFirst { it == primaryColor }
                        val index = if (lastIndex == colorArray.lastIndex) 0 else lastIndex + 1
                        primaryColor = colorArray[index]
                        secondaryColor = getInverseBWColor(colorArray[index])
                        statusBarColor.value = colorArray[index]
                    },
                    onShareCaptured = {
                        shareImage(it)
                    }
                )
            }
        }
    }

    private fun shareImage(bitmap: Bitmap) {
        file = File.createTempFile("share", ".jpg", requireActivity().cacheDir)
        val outputStream: OutputStream
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            return
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

    private fun getInverseBWColor(color: Int): Int {
        val red = Color.red(ContextCompat.getColor(requireContext(), color))
        val blue = Color.blue(ContextCompat.getColor(requireContext(), color))
        val green = Color.green(ContextCompat.getColor(requireContext(), color))

        val luminance = (red * 0.299) + (green * 0.7152) + (blue * 0.0722)
        return if (luminance < 140) R.color.white else R.color.black
    }

    override fun onResume() {
        super.onResume()
        file?.delete()
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
    }
}