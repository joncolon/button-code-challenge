import android.content.Context
import android.util.Log
import android.widget.Toast

fun executeInThread(function: () -> Unit) {
    Thread({ function() }).start()
}

fun Any.DEBUG(message: String) {
    Log.d(javaClass.simpleName, message)
}

fun Any.ERROR(message: String) {
    Log.e(javaClass.simpleName, message)
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()