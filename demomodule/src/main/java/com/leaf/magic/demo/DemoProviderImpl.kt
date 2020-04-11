package com.leaf.magic

import com.leaf.magic.annotation.Provider


@Provider(provider = DemoProvider::class)
class DemoProviderImpl : DemoProvider {
    override fun getDemoName(): String {
        return "module name is demo"
    }
}