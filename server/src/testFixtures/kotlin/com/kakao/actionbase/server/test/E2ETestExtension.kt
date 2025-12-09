package com.kakao.actionbase.server.test

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class E2ETestExtension :
    BeforeAllCallback,
    AfterAllCallback {
    override fun beforeAll(context: ExtensionContext) {
    }

    override fun afterAll(context: ExtensionContext) {
    }
}
