// IFT 1025 session Automne 2022
// Byungsuk Min et Yilin Zang
// 10/26/2022
// TP1 : MiniCorrecteur

import Vue.CorrecteurVue;

public class Correcteur
{
    private CorrecteurVue correcteurVue;
    public Correcteur(String title)
    {
        this.correcteurVue = new CorrecteurVue(title);
    }

    public void creerEtAfficher()
    {
        correcteurVue.afficher();
    }

    // Lancer notre programme
    public static void main(String[] args)
    {
        Correcteur correcteur = new Correcteur("MiniCorrecteur");
        correcteur.creerEtAfficher();
    }
}
