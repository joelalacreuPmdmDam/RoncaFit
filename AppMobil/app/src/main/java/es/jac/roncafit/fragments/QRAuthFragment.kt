package es.jac.roncafit.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import es.jac.roncafit.R
import es.jac.roncafit.databinding.FragmentQRAuthBinding


class QRAuthFragment : Fragment() {

    private lateinit var binding: FragmentQRAuthBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorBackground)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQRAuthBinding.inflate(inflater,container,false)

        // Información del usuario
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("auth_nombreUsuario", null)
        val userId = sharedPreferences.getInt("auth_idCliente", -1)
        if (userName == null || userId == -1) {
            Toast.makeText(requireContext(), "Información del usuario no disponible", Toast.LENGTH_SHORT).show()
        }else{
            binding.tvUserName.text = userName
            binding.tvIdCliente.text = userId.toString()
            val qrData = "ID: $userId\nNombre: $userName"
            val qrCodeBitmap = generateQRCode(qrData)
            qrCodeBitmap?.let {
                binding.qrCodeImageView.setImageBitmap(it)
            }
        }



        return binding.root
    }

    private fun generateQRCode(data: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 250, 250)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) -0x1000000 else -0x1)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        // Restaurar el estado de los iconos en la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility = 0
        }
    }



}