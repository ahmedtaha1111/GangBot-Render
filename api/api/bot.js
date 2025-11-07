// api/bot.js - بوت بسيط (هنضيف الأوامر بعدين)
const { Client, GatewayIntentBits } = require('discord.js');

let client;

async function startBot() {
  if (client) return client;

  client = new Client({
    intents: [
      GatewayIntentBits.Guilds,
      GatewayIntentBits.GuildMessages,
      GatewayIntentBits.MessageContent,
      GatewayIntentBits.GuildMembers
    ]
  });

  client.once('ready', () => {
    console.log(`البوت شغال: ${client.user.tag}`);
  });

  client.on('messageCreate', (msg) => {
    if (msg.content === '!test') {
      msg.reply('البوت شغال يا معلم! ✅');
    }
  });

  await client.login(process.env.DISCORD_TOKEN);
  return client;
}

module.exports = { startBot };
