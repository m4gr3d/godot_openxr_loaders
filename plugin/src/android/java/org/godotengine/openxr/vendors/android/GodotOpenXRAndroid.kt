/**************************************************************************/
/*  GodotOpenXRAndroid.kt                                                 */
/**************************************************************************/
/*                       This file is part of:                            */
/*                              GODOT XR                                  */
/*                      https://godotengine.org                           */
/**************************************************************************/
/* Copyright (c) 2022-present Godot XR contributors (see CONTRIBUTORS.md) */
/*                                                                        */
/* Permission is hereby granted, free of charge, to any person obtaining  */
/* a copy of this software and associated documentation files (the        */
/* "Software"), to deal in the Software without restriction, including    */
/* without limitation the rights to use, copy, modify, merge, publish,    */
/* distribute, sublicense, and/or sell copies of the Software, and to     */
/* permit persons to whom the Software is furnished to do so, subject to  */
/* the following conditions:                                              */
/*                                                                        */
/* The above copyright notice and this permission notice shall be         */
/* included in all copies or substantial portions of the Software.        */
/*                                                                        */
/* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        */
/* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     */
/* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. */
/* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY   */
/* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,   */
/* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE      */
/* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                 */
/**************************************************************************/

package org.godotengine.openxr.vendors.android

import org.godotengine.godot.Godot
import org.godotengine.godot.utils.PermissionsUtil
import org.godotengine.openxr.vendors.GodotOpenXR

/**
 * Godot OpenXR plugin for the Android XR platform.
 */
class GodotOpenXRAndroid(godot: Godot?) : GodotOpenXR(godot) {
    companion object {
        /**
         * Representing the user's eye pose and orientation, for the purposes of avatars
         */
        private const val EYE_TRACKING_PERMISSION = "android.permission.EYE_TRACKING"

        /**
         * Eye gaze input and interactions
         */
        private const val EYE_TRACKING_FINE_PERMISSION = "android.permission.EYE_TRACKING_FINE"

        /**
         * Tracking and rendering facial expressions
         */
        private const val FACE_TRACKING_PERMISSION = "android.permission.FACE_TRACKING"

        /**
         * Tracking hand joint poses and angular and linear velocities; Using a mesh representation of the user's hands
         */
        private const val HAND_TRACKING_PERMISSION = "android.permission.HAND_TRACKING"

        /**
         * - Light estimation
         * - projecting passthrough onto mesh surfaces
         * - performing raycasts against trackables in the environment
         * - plane tracking
         * - object tracking
         * - working with depth for occlusion and hit testing
         * - persistent anchors
         */
        private const val SCENE_UNDERSTANDING_PERMISSION = "android.permission.SCENE_UNDERSTANDING"

        private val ANDROID_XR_PERMISSIONS_LIST = listOf(
            EYE_TRACKING_PERMISSION,
            EYE_TRACKING_FINE_PERMISSION,
            FACE_TRACKING_PERMISSION,
            HAND_TRACKING_PERMISSION,
            SCENE_UNDERSTANDING_PERMISSION,
        )
    }
    override fun getPluginName() = "GodotOpenXRAndroid"

    override fun getPluginPermissionsToEnable(): MutableList<String> {
        val permissionsToEnable = super.getPluginPermissionsToEnable()

        // Go through the list of permissions, and request the ones that are included in the
        // manifest.
        for (permission in ANDROID_XR_PERMISSIONS_LIST) {
            if (PermissionsUtil.hasManifestPermission(activity, permission)) {
                permissionsToEnable.add(permission)
            }
        }
        return permissionsToEnable
    }

    override fun supportsFeature(featureTag: String): Boolean {
        if ("PERMISSION_XR_EXT_eye_gaze_interaction" == featureTag) {
            val grantedPermissions = godot?.getGrantedPermissions()
            if (grantedPermissions != null) {
                for (permission in grantedPermissions) {
                    if (EYE_TRACKING_FINE_PERMISSION == permission) {
                        return true
                    }
                }
            }
        }
        return false
    }
}