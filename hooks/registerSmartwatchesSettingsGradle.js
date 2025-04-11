const fs = require('fs');
const path = require('path');

module.exports = function (context) {
  const platformRoot = path.join(context.opts.projectRoot, 'platforms', 'android');
  const settingsGradle = path.join(platformRoot, 'settings.gradle');

  const includeSmartwatch = `include ':smartwatch'`;

  if (fs.existsSync(settingsGradle)) {
    let content = fs.readFileSync(settingsGradle, 'utf-8');

    if (!content.includes(includeSmartwatch)) {
      // Insere após o último include existente
      const lastIncludeRegex = /include\s+['"][^'"]+['"]/g;
      const matches = [...content.matchAll(lastIncludeRegex)];
      if (matches.length > 0) {
        const lastMatch = matches[matches.length - 1];
        const insertPos = lastMatch.index + lastMatch[0].length;
        const updatedContent = content.slice(0, insertPos) +
          `\n${includeSmartwatch}\n` +
          content.slice(insertPos);

        fs.writeFileSync(settingsGradle, updatedContent, 'utf-8');
        console.log('✅ Linha "include \':smartwatch\'" adicionada ao settings.gradle');
      } else {
        // Se não encontrar includes, adiciona ao final
        fs.appendFileSync(settingsGradle, `\n${includeSmartwatch}\n`);
        console.log('>>>>> ✅ settings.gradle atualizado (append)');
      }
    } else {
      console.log('>>>>>> ℹ️ settings.gradle já contém ":smartwatch"');
    }
  } else {
    console.warn('>>>>>> ⚠️ settings.gradle não encontrado');
  }
};