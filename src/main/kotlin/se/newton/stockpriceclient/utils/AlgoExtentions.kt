package se.newton.stockpriceclient.utils

import com.algorand.algosdk.v2.client.common.Response

inline fun <reified T> Response<T>.extractOrFail(
	errorMsg: String = "Failed to extract value ${T::class.simpleName}"
) : T {
	if (!isSuccessful) {
		throw Exception(errorMsg)
	}
	return body()
}