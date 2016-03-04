package meh.expo;

import de.robv.android.xposed.SELinuxHelper;
import de.robv.android.xposed.services.BaseService;

class SELinux {
	companion object {
		fun isEnabled(): Boolean {
			return SELinuxHelper.isSELinuxEnabled()
		}

		fun isEnforced(): Boolean {
			return SELinuxHelper.isSELinuxEnforced()
		}

		fun context(): String {
			return SELinuxHelper.getContext()
		}

		fun service(): BaseService {
			return SELinuxHelper.getAppDataFileService()
		}
	}
}
