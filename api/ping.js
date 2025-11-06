// api/ping.js
export default function handler(req, res) {
  res.status(200).json({
    status: "ok",
    message: "pong!",
    bot: "gang-bot-render",
    time: new Date().toISOString(),
    uptime: process.uptime()
  });
}
