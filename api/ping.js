// api/ping.js - يحافظ على البوت صاحي
let botStarted = false;

module.exports = async (req, res) => {
  if (!botStarted) {
    try {
      await require('./bot.js').startBot();
      botStarted = true;
      console.log("البوت شغال!");
    } catch (err) {
      console.error("خطأ في البوت:", err);
    }
  }

  res.status(200).json({
    status: "ok",
    message: "pong!",
    bot: "PURE GANGS",
    time: new Date().toISOString()
  });
};
