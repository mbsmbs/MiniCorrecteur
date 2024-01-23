package Modele;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import Vue.CorrecteurVue;

public class CorrecteurModele extends Component
{
    // Attributs
    private CorrecteurVue vue;
    private JFileChooser dictionnaire, fichier;
    private ArrayList<String> motsDict, motsTxt;
    private ArrayList<String> mots5Proches;

    // Constructeur : Initialisation des attributs
    public CorrecteurModele(CorrecteurVue vue)
    {
        this.vue = vue;
        dictionnaire = new JFileChooser();
        fichier = new JFileChooser();
        motsDict = new ArrayList<String>();
        motsTxt = new ArrayList<String>();
    }

    // Getter
    public CorrecteurVue getVue()
    {
        return vue;
    }

    // Ouvrir un fichier
    public void ouvrirFich()
    {
        fichier.setCurrentDirectory(new File("."));
        int val = this.fichier.showOpenDialog(this);

        try {
            if (val == 0)
            {
                BufferedReader r = new BufferedReader(new FileReader(fichier.getSelectedFile()));
                String line = null;

                while((line = r.readLine()) != null) {
                    motsTxt.add(line);
                }

                vue.afficherTxt(motsTxt);

                r.close();
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    // Enregistrer un texte dans un fichier
    public void enregistrerFich()
    {
        JFileChooser fichier = new JFileChooser();
        fichier.setCurrentDirectory(new File("."));
        int option = fichier.showSaveDialog(this);
        if(option != JFileChooser.APPROVE_OPTION){return;}

        File fileName = new File(fichier.getSelectedFile() + ".txt");
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(fileName));
            outFile.write(vue.getTextArea().getText());
        }catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            if(outFile !=null){
                try {
                    outFile.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Charger un dictionnaire
    public void chargerDict()
    {
        dictionnaire.setCurrentDirectory(new File("."));
        int val = this.dictionnaire.showOpenDialog(this);

        try {
            if (val == 0)
            {
                BufferedReader r = new BufferedReader(new FileReader(this.dictionnaire.getSelectedFile()));
                String line = null;

                while((line = r.readLine()) != null) {
                    this.motsDict.add(line);
                }

                r.close();
            }
        }
        catch (Exception var4)
        {
            var4.printStackTrace();
        }
    }

    // Vérifier les erreurs d'orthographe du texte et demande CorrecteurVue de surligner les erreurs
    public void verifTxt()
    {
        vue.surlignerMots();
    }

    // Méthode utilisée dans surlignerMots() de CorrecteurVue pour vérifier si les mots existent dans le dictionnaire
    public boolean motExistDict(String mot)
    {
        return motsDict.contains(mot);
    }

    // Comparer la distance entre le mot du texte avec les mots du dictionnaire pour trouver 5 mots les + proches
    public void propose5MotsProches(String selectedMot, int positionX, int positionY)
    {
        mots5Proches = new ArrayList<String>();

        int distanceMinimum = 100000;
        for(String mot : motsDict)
        {
            int distance = distance(selectedMot, mot);
            distanceMinimum = Math.min(distanceMinimum, distance);
        }

        while (mots5Proches.size() < 5)
        {
            for(String mot : motsDict)
            {
                if(mots5Proches.size() == 5)
                {
                    break;
                }
                else if(distance(selectedMot, mot) == distanceMinimum)
                {
                    mots5Proches.add(mot);
                }
            }
            distanceMinimum++;
        }

        vue.afficher5MotsProches(mots5Proches, positionX, positionY);
    }

    // Méthode utilisée dans propose5MotsProches() pour calculer la distance entre 2 mots
    public int distance(String s1, String s2)
    {
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        for(int i=0;i<=s1.length();i++)
            edits[i][0]=i;
        for(int j=1;j<=s2.length();j++)
            edits[0][j]=j;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(
                        edits[i-1][j]+1,
                        Math.min(
                                edits[i][j-1]+1,
                                edits[i-1][j-1]+u
                        )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }
}
