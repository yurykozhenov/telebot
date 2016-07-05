# External registration

Hello, pal! Now you can provide to your pilots very simple registration process using your own website and users database.

To register user you must:
- send POST request to telebot server endpoint `POST http://your.telebot/register` with body
`
{
    "id":100100,
    "name":"Ingame Name",
    "secret":"somesecret"
}
`
- receive some key such as `ab674f` and give it to user with link to telebot

Your user will open telebot chat and write key into. Registration is complete!
