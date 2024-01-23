package Vue;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import Controle.CorrecteurControle;
import Modele.CorrecteurModele;

public class CorrecteurVue extends JFrame
{
    // Attributs
    private CorrecteurModele modele;
    private CorrecteurControle controle;
    private JMenuBar menuBar;
    private JMenu fichMenu, dictMenu, verifMenu;
    private JMenuItem ouvrirFichItem, enregistrerFichItem, chargerDictItem, verifOrthoItem;
    private JPopupMenu menu5MotsProches;
    private ArrayList<JMenuItem> itemsMots5Proches;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Highlighter.HighlightPainter monSurligneur;

    // Constructeur -> Créer : menubar + textarea & Ajout : event listners. & Initialisation des attributs
    public CorrecteurVue(String title)
    {
        super(title);

        this.setLayout(new BorderLayout());

        menuBar = new JMenuBar();

        fichMenu = new JMenu("Fichier");
        dictMenu = new JMenu("Dictionnaire");
        verifMenu = new JMenu("Vérifier");

        ouvrirFichItem = new JMenuItem("Ouvrir");
        enregistrerFichItem = new JMenuItem("Enregistrer");
        fichMenu.add(ouvrirFichItem);
        fichMenu.add(enregistrerFichItem);

        chargerDictItem = new JMenuItem("Charger");
        dictMenu.add(chargerDictItem);

        verifOrthoItem = new JMenuItem("Lancer");
        verifMenu.add(verifOrthoItem);

        menuBar.add(fichMenu);
        menuBar.add(dictMenu);
        menuBar.add(verifMenu);

        this.setJMenuBar(menuBar);

        textArea = new JTextArea();
        monSurligneur = new MonSurligneurRouge(Color.red);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);

        modele = new CorrecteurModele(this);
        controle = new CorrecteurControle(modele);

        ouvrirFichItem.addActionListener(controle.new Fichier());
        enregistrerFichItem.addActionListener(controle.new Fichier());
        chargerDictItem.addActionListener(controle.new Dictionnaire());
        verifOrthoItem.addActionListener(controle.new ProfFrancais());

        textArea.addMouseListener(controle.new DetecteurRouge());
    }
    // Getters
    public JTextArea getTextArea()
    {
        return this.textArea;
    }
    public JMenuItem getOuvrirItem()
    {
        return ouvrirFichItem;
    }
    public JMenuItem getEnregistrerItem()
    {
        return enregistrerFichItem;
    }

    // Affiche la fenêtre
    public void afficher()
    {
        this.setDefaultCloseOperation( EXIT_ON_CLOSE );
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Afficher le texte d'un fichier
    public void afficherTxt(ArrayList<String> motsTxt)
    {
        if (motsTxt != null) {
            Iterator var1 = motsTxt.iterator();

            while(var1.hasNext()) {
                String s = (String)var1.next();
                this.textArea.append(s + "\n");
            }
        }
    }

    // Proposer 5 mots les plus proches
    public void afficher5MotsProches(ArrayList<String> mots5Proches, int positionX, int positionY)
    {
        menu5MotsProches = new JPopupMenu();

        for(String mot : mots5Proches)
        {
            JMenuItem motProche = new JMenuItem(mot);
            itemsMots5Proches = new ArrayList<JMenuItem>();
            motProche.addActionListener(controle.new ProposeMots());
            itemsMots5Proches.add(motProche);
            menu5MotsProches.add(motProche);
        }

        menu5MotsProches.show(textArea, positionX, positionY);
    }

    // Surligner les motes en rouge
    public void surlignerMots()
    {
        effaceSurligne(textArea);

        try {
            Highlighter hilite = textArea.getHighlighter();
            Document fichier = textArea.getDocument();
            String text = fichier.getText(0, fichier.getLength());

            ArrayList<String> motsTxt = new ArrayList<>(Arrays.asList(text.split("[\\s\\n]+")));

            for(String mot : motsTxt)
            {
                int pos = 0;
                if(!modele.motExistDict(mot.toLowerCase()))
                {
                    while ((pos = text.indexOf(mot, pos)) >= 0)
                    {
                        hilite.addHighlight(pos, pos+mot.length(), monSurligneur);
                        pos += mot.length();
                    }
                }
            }

        } catch (BadLocationException e) {
        }
    }

    // Enlever les surligneurs
    public void effaceSurligne(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MonSurligneurRouge) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

}

// Surligneur rouge
class MonSurligneurRouge extends DefaultHighlighter.DefaultHighlightPainter
{
    public MonSurligneurRouge(Color color) {
        super(color);
    }
}