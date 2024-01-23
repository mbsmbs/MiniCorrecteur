package Controle;

import javax.swing.*;
import javax.swing.text.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Modele.CorrecteurModele;

public class CorrecteurControle
{
    // Attributs
    private CorrecteurModele modele;
    private int offset, motChoisiDebut, motChoisiFin;
    private String motChoisi;

    // Contructeur : initialisation d'un attribut
    public CorrecteurControle(CorrecteurModele modele)
    {
        this.modele = modele;
    }

    // Classe interne : Écoute et traite les événements sur les menus «ouvrir» et «enregistrer»
    public class Fichier implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == modele.getVue().getOuvrirItem())
            {
                modele.ouvrirFich();
            }
            else if(e.getSource() == modele.getVue().getEnregistrerItem())
            {
                modele.enregistrerFich();
            }
        }
    }

    // Classe interne : Écoute et traite les événements sur le menu «charger»
    public class Dictionnaire implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            modele.chargerDict();
        }
    }

    // Classe interne : Écoute et traite les événements sur le menu «vérifier»
    public class ProfFrancais implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            modele.verifTxt();
        }
    }

    // Classe interne : Écoute et traite les événements du bouton droit du souris dans le textarea
    public class DetecteurRouge implements MouseListener
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            try{
                if(SwingUtilities.isRightMouseButton(e))
                {
                    JTextArea textArea = modele.getVue().getTextArea();
                    offset = textArea.viewToModel2D(e.getPoint());
                    motChoisiDebut = Utilities.getWordStart(textArea, offset);
                    motChoisiFin = Utilities.getWordEnd(textArea, offset);
                    motChoisi = textArea.getDocument().getText(motChoisiDebut, motChoisiFin - motChoisiDebut);
                    if(!modele.motExistDict(motChoisi))
                    {
                        modele.propose5MotsProches(motChoisi, e.getX(), e.getY());
                    }
                }
            } catch(Exception e2){

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        @Override
        public void mouseEntered(MouseEvent e) {

        }
        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    // Classe interne : Écoute et traite les événements de la sélection du menu dans le textarea
    public class ProposeMots implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea textArea = modele.getVue().getTextArea();
            String motCorrect = e.getActionCommand();
            if(motChoisiDebut >= 0 && motChoisi.length() > 0)
            {
                textArea.replaceRange(motCorrect, motChoisiDebut, motChoisiDebut + motChoisi.length());
            }
            modele.verifTxt();
        }
    }
}
