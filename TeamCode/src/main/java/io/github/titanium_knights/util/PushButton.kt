package io.github.titanium_knights.util


//class PushButton(val readState: () -> Boolean): PassdionComponent {
//    var previousState: Boolean = false
//    var currentState: Boolean = readState()
//
//    val isPressed: Boolean get() {
//        return currentState && !previousState
//    }
//
//    fun update() {
//        previousState = currentState
//        currentState = readState()
//    }
//
//    override fun init(opMode: PassdionOpMode) {}
//    override fun initLoop(opMode: PassdionOpMode) {}
//    override fun start(opMode: PassdionOpMode) {}
//    override fun update(opMode: PassdionOpMode) {
//        update()
//    }
//    override fun cleanup(opMode: PassdionOpMode) {}
//}
//
//class ToggleButton(readState: () -> Boolean): PassdionComponent {
//    val pushButton = PushButton(readState)
//
//    var isActive = false
//
//    fun update() {
//        if (pushButton.isPressed) {
//            isActive = !isActive
//        }
//        pushButton.update()
//    }
//
//    override fun init(opMode: PassdionOpMode) {}
//    override fun initLoop(opMode: PassdionOpMode) {}
//    override fun start(opMode: PassdionOpMode) {}
//    override fun update(opMode: PassdionOpMode) {
//        update()
//    }
//    override fun cleanup(opMode: PassdionOpMode) {}
//}