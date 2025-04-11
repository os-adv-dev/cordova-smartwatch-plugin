# Cordova Smartwatch Plugin

This is a Cordova plugin designed to facilitate communication and dynamic UI rendering between a mobile device and a standalone Wear OS smartwatch module.

## Features

- Automatically creates and includes a `:smartwatch` module in your Android build.
- Connects the phone and watch using `WearableClient`.
- Receives and renders dynamic UI payloads sent from the mobile app.
- Supports screens with texts, buttons, and images (including Base64 and URLs).
- Hooks to configure Gradle for Wear OS integration (`wearApp project(":smartwatch")`).

## Installation

```bash
cordova plugin add https://github.com/os-adv-dev/cordova-smartwatch-plugin.git
```

This plugin will:
- Copy the Wear OS module (`src/android/smartwatch`) into the project
- Include it in `settings.gradle`
- Add `wearApp project(':smartwatch')` in `app/build.gradle` under the correct `dependencies` section.

> ⚠️ It will **not** add the line if you already manage your own build types or modules.

## Usage

### Send Template to Smartwatch

The phone app can send a dynamic JSON template that the Wear OS app will render using Jetpack Compose:

```js
const template = {
  home: {
    content: {
      title: "Welcome!",
      description: "Click below to see details",
      list: [
        {
          text: "See details",
          action: "detail"
        }
      ]
    }
  },
  detail: {
    content: {
      title: "Detail screen",
      description: "This is the detail screen",
      image: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAATkAAACh..."
    }
  }
};

if (cordova.plugins && cordova.plugins.SmartwatchPlugin) {
    cordova.plugins.SmartwatchPlugin.sendTemplate(
      JSON.stringify(template),
      success => console.log("✅ Sent", success),
      err => console.error("❌ Error", err)
    );
} else {
    console.warn("⚠️ Plugin Smartwatch not found");
}
```

## Payload Structure

Each screen (e.g., `home`, `detail`) can include the following:

- `title` (string)
- `description` (string)
- `list` (array of objects with `text` and `action`)
- `image` (optional - Base64, URL, or placeholder)


## License

MIT