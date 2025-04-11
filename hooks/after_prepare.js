const fs = require('fs');
const path = require('path');

module.exports = function (context) {
  const platformRoot = path.join(context.opts.projectRoot, 'platforms', 'android');
  const settingsGradle = path.join(platformRoot, 'settings.gradle');
  const smartwatchInclude = "\ninclude ':smartwatch'\nproject(':smartwatch').projectDir = new File(rootProject.projectDir, 'smartwatch')\n";

  // Atualizar settings.gradle
  if (fs.existsSync(settingsGradle)) {
    let content = fs.readFileSync(settingsGradle, 'utf-8');
    if (!content.includes("include ':smartwatch'")) {
      fs.appendFileSync(settingsGradle, smartwatchInclude);
      console.log('✅ settings.gradle atualizado com módulo :smartwatch');
    } else {
      console.log('ℹ️ settings.gradle já contém o módulo :smartwatch');
    }
  }
};