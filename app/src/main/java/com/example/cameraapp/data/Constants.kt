package com.example.cameraapp.data

object Constants {
    const val APP_MIN_PASSWORD_LENGTH = 8

    const val APP_PREFERENCES = "APP_PREFERENCES"
    const val APP_PREFERENCES_STAY = "APP_PREFERENCES_STAY_BOOL"
    const val APP_PREFERENCES_PUSHES = "APP_PREFERENCES_PUSHES_BOOL"

    const val APP_TOAST_WEAK_CONNECTION = "Looks like there are some problems with connection..."
    const val APP_TOAST_NOT_SIGNED_IN = "You aren't signed in yet."
    const val APP_TOAST_LOGIN_FAILED = "Failed to log in"
    const val APP_TOAST_LOGIN_SUCCESS = "Logged in successfully"
    const val APP_TOAST_SIGNUP_FAILED = "Failed to sign up"
    const val APP_TOAST_SIGNUP_SUCCESS = "Registered successfully"
    const val APP_TOAST_PASSWORD_TOO_SHORT = "Your password should be at least $APP_MIN_PASSWORD_LENGTH characters long."
    const val APP_TOAST_PASSWORDS_DONT_MATCH = "Your passwords don't match. Please confirm your password."

    const val APP_CODE_CAMERA_SELECT_PHOTO = 32768
    const val APP_CODE_CAMERA_TAKE_PHOTO = 65536

    const val ACTION_VIDEO_SWITCH_SCREEN = "switch_screen"
    const val ACTION_VIDEO_TURN_LEFT = "turn_left"
    const val ACTION_VIDEO_TURN_RIGHT = "turn_right"
    const val ACTION_CAMERA_PHOTO_TAKE = "take_photo"
    const val ACTION_CAMERA_PHOTO_PREVIOUS = "switch_photo_previous"
    const val ACTION_CAMERA_PHOTO_NEXT = "switch_photo_next"

    const val MQTT_SERVER_URI = "tcp://wqtt.ru"
    const val MQTT_SERVER_PORT = "5031"

    const val MQTT_CLIENT_ID = "xdtcgfvgyjuhijokcfghjkj101"

    const val MQTT_USER_NAME = "123"
    const val MQTT_USER_PASSWORD = "123"

    private const val MQTT_TOPIC_MAIN = "cameradev"

    const val MQTT_TOPIC_POWER = "$MQTT_TOPIC_MAIN/power/mode"

    const val MQTT_TOPIC_ACTION_CAMERA = "$MQTT_TOPIC_MAIN/action/camera"
    const val MQTT_TOPIC_ACTION_VIDEO = "$MQTT_TOPIC_MAIN/action/video"

    val MQTT_TOPIC_LIST = listOf(MQTT_TOPIC_POWER)

    const val APP_DB_PROPER_NAME = "camera_info_database"
}
