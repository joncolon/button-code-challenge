package colon.jonathan.buttoncodechallenge.injection

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@kotlin.annotation.Retention(RUNTIME)
annotation class DatabaseInfo
