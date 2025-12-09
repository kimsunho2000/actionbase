package com.kakao.actionbase.v2.engine.exception

import com.kakao.actionbase.v2.engine.GraphException

class MutationError : GraphException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
