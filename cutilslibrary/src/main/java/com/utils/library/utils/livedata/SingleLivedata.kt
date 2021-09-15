package com.utils.library.utils.livedata

import java.util.concurrent.atomic.AtomicBoolean
import java.lang.ref.WeakReference
import java.util.ArrayList
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SingleLivedata<T> : MutableLiveData<T?>() {
    private val mPending = AtomicBoolean(false)
    private val ownerClassNames = WeakReference(ArrayList<String>())
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
        val className = owner.javaClass.name
        if (ownerClassNames.get() != null) {
            if (ownerClassNames.get()!!.contains(className)) {
                return
            } else {
                ownerClassNames.get()!!.add(className)
            }
        }
//        if (hasActiveObservers()){
//
//        }
        super.observe(owner, { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    private fun call() {
        value = null
    }

    public override fun onInactive() {
        if (!hasActiveObservers()) ownerClassNames.clear()
    }
}