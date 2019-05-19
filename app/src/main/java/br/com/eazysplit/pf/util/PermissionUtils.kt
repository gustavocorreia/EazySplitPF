package br.com.eazysplit.pf.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


object PermissionUtils{

    fun validarPremissoes(permissoes: List<String>, activity: Activity, requestCode:Int):Boolean{

        val listPermissoes = ArrayList<String>()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(permissao in permissoes){
                val temPermissao = ContextCompat.checkSelfPermission(activity,permissao) ==
                        PackageManager.PERMISSION_GRANTED
                if(!temPermissao) listPermissoes.add(permissao)
            }
            if(listPermissoes.isEmpty()) return true
            else{
                //   val novasPermissoes = arrayOfNulls<String>(listPermissoes.size)
                ActivityCompat.requestPermissions(activity,listPermissoes.toTypedArray(),requestCode )
            }

        }
        return  true
    }




}
