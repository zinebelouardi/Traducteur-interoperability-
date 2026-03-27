<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Traduction en Darija</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
        }
        h1 {
            font-size: 24px;
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }
        .container {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
            display: flex;
            flex-direction: column;
        }
        .chat-box {
            flex-grow: 1;
            overflow-y: auto;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 10px;
            border: 1px solid #ddd;
            height: 300px;
            display: flex;
            flex-direction: column;
        }
        .message {
            padding: 10px 15px;
            margin: 5px 0;
            border-radius: 20px;
            max-width: 70%;
            word-wrap: break-word;
        }
        .sent {
            background-color: #ffd6e7;
            align-self: flex-end;
        }
        .received {
            background-color: #d4f4d7;
            align-self: flex-start;
        }
        .input-section {
            display: flex;
            gap: 10px;
            align-items: center;
            margin-top: 10px;
        }
        input[type="text"] {
            flex-grow: 1;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 20px;
        }
        button {
            background-color: #007bff;
            color: white;
            font-size: 14px;
            padding: 10px 20px;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>Traduction en Darija</h1>
    <div class="container">
        <div class="chat-box">
            <?php
            if ($_SERVER['REQUEST_METHOD'] === 'POST') {
                $text = $_POST['text'];
                $url = "http://localhost:8080/zianeb/rest/translate?inputText=".urlencode($text);
                $response = @file_get_contents($url);

                echo '<div class="message sent">' . htmlspecialchars($text) . '</div>';

                if ($response !== false) {
                    $decodedResponse = json_decode($response, true);
                    if (isset($decodedResponse['translatedText'])) {
                        echo '<div class="message received">' . htmlspecialchars($decodedResponse['translatedText']) . '</div>';
                    } else {
                        echo '<div class="message received">Erreur: Réponse inattendue du serveur.</div>';
                    }
                } else {
                    echo '<div class="message received">Erreur de connexion au serveur.</div>';
                }
            }
            ?>
        </div>
        <form method="POST" class="input-section">
            <input type="text" id="text" name="text" placeholder="Entrez votre message...">
            <button type="submit">Traduire</button>
        </form>
    </div>
</body>
</html>
