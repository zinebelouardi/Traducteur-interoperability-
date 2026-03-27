import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';
import axios, { AxiosError } from 'axios';

const App = () => {
  const [inputText, setInputText] = useState<string>('');  // Texte à traduire
  const [translatedText, setTranslatedText] = useState<string>('');  // Résultat de la traduction
  const [error, setError] = useState<string>('');  // Erreurs possibles

  // Fonction pour gérer le texte de l'input
  const handleInputChange = (text: string) => {  // Paramètre 'text' typé
    setInputText(text);
    setError(''); // Réinitialiser l'erreur
  };

  // Fonction pour effectuer la traduction
  const handleTranslate = async () => {
    if (!inputText.trim()) {
      setError('Veuillez entrer un texte.');
      return;
    }

    try {
      const response = await axios.get(`https://01b4-105-156-59-201.ngrok-free.app/zianeb/rest/translate?inputText=${encodeURIComponent(inputText)}`);
      console.log("Réponse complète de l'API:", response.data);

      // Vérifier si la traduction est présente dans la réponse
      if (response && response.data && response.data.translatedText) {
        setTranslatedText(response.data.translatedText);  // Mettre à jour la traduction
        setError('');  // Réinitialiser les erreurs
      } else {
        throw new Error('Réponse de l\'API invalide');
      }
    } catch (err) {
      // Vérification que err est une instance de AxiosError
      if (axios.isAxiosError(err)) {
        console.error('Erreur complète d\'Axios:', err);

        if (err.response) {
          console.log("Réponse de l'API :", err.response);
          setError(`Erreur API : ${err.response.status} - ${err.response.data}`);
        } else if (err.request) {
          setError('Erreur de requête : Impossible de contacter le serveur');
        } else {
          setError(`Erreur inconnue : ${err.message}`);
        }
      } else {
        // Si ce n'est pas une erreur d'Axios, afficher le message d'erreur générique
        console.error('Erreur inconnue:', err);
        setError('Erreur inconnue');
      }
      setTranslatedText('');  // Réinitialiser le texte traduit en cas d'erreur
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Traduction en Darija</Text>
      <TextInput
        style={styles.input}
        value={inputText}
        onChangeText={handleInputChange}
        placeholder="Entrez un texte"
      />
      <Button title="Traduire" onPress={handleTranslate} />

      {/* Affichage de l'erreur ou de la traduction */}
      {error && <Text style={styles.error}>{error}</Text>}
      {translatedText && <Text style={styles.translation}>Traduction: {translatedText}</Text>}
    </View>
  );
};

// Styles
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  input: {
    width: '100%',
    padding: 10,
    borderColor: '#ccc',
    borderWidth: 1,
    borderRadius: 5,
    marginBottom: 20,
  },
  error: {
    color: 'red',
    marginTop: 10,
  },
  translation: {
    marginTop: 20,
    fontWeight: 'bold',
  },
});

export default App;
