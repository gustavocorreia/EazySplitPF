// baseado no código em https://pt.stackoverflow.com/questions/207341/mascara-de-data-no-edittext
package br.com.eazysplit.pf.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object Mask {

    fun insert(et:EditText, mask:String):TextWatcher {

        return object:TextWatcher {
            internal var isUpdating:Boolean = false
            internal var oldText = ""

            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {
                val str = Mask.unmask(s.toString())
                var maskCurrent = ""

                if (isUpdating) {
                    oldText = str
                    isUpdating = false
                    return
                }

                var i = 0
                for (m in mask.toCharArray()) {

                    if (m != '#' && str.length > oldText.length) {
                        maskCurrent += m
                        continue
                    }

                    try {
                        maskCurrent += str.get(i)
                    } catch (e:Exception) {
                        break
                    }
                    i++
                }

                isUpdating = true
                et.setText(maskCurrent)
                et.setSelection(maskCurrent.length)
            }

            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {}

            override fun afterTextChanged(s:Editable) {}
        }
    }

    fun unmask(s:String):String {

        return s.replace(("[.]").toRegex(), "").replace(("[-]").toRegex(), "")
            .replace(("[/]").toRegex(), "").replace(("[(]").toRegex(), "")
            .replace(("[)]").toRegex(), "")

    }
}