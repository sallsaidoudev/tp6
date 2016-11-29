package fr.istic.prg1.tree;

import java.util.Scanner;

import fr.istic.prg1.tree.util.AbstractImage;
import fr.istic.prg1.tree.util.Iterator;
import fr.istic.prg1.tree.util.Node;
import fr.istic.prg1.tree.util.NodeType;

/**
 * Groupe 1.1
 * NGUYEN NHON Bérenger & IQUEL Erwan
 */

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2016-04-20
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public class Image extends AbstractImage {
	private static final Scanner standardInput = new Scanner(System.in);

	public Image() {
		super();
	}

	public static void closeAll() {
		standardInput.close();
	}

	/**
	 * @param x
	 *            abscisse du point
	 * @param y
	 *            ordonnée du point
	 * @pre 	!this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y) {
		try {
			assert (!isEmpty()) : "L'image est vide";
			Iterator<Node> it = iterator();
			explore_search (it, x, y, 'h', WINDOW_SIZE, WINDOW_SIZE);
			return it.getValue().state == 1;
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	/**
	 * Recherche le noeud de l'arbre contenant le point dont les coordonnees sont passees en parametre.
	 * Apres l'appel a cette fonction, ce noeud est le noeud courant de l'iterateur.
	 * 
	 * @param it
	 * 			iterateur sur l'arbre a parcourir
	 * @param x
	 * 			abscisse du point a rechercher
	 * @param y
	 * 			ordonnee du point a rechercher
	 * @param cutType
	 * 			sens du decoupage au noeud courant de it : 'v' pour vertical ; 'h' pour horizontal
	 * @param x_size
	 * 			largeur de la portion de fenetre correspondant au noeud courant de it
	 * @param y_size
	 * 			hauteur de la portion de fenetre correspondant au noeud courant de it
	 */
	private void explore_search (Iterator<Node> it, int x, int y, char cutType, int x_size, int y_size) {
		if (it.nodeType() != NodeType.LEAF) {
			if (cutType == 'v') {
				x_size = x_size / 2;
				if (x < x_size) it.goLeft();
				else it.goRight();
				explore_search (it, x, y, 'h', x_size, y_size);
			} else {
				y_size = y_size / 2;
				if (y < x_size) it.goLeft();
				else it.goRight();
				explore_search (it, x, y, 'v', x_size, y_size);
			}
		}
	}
	
	/**
	 * this devient identique à image2.
	 *
	 * @param image2
	 *            image à copier
	 *
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";
			
			Iterator<Node> it = iterator(),
					it2 = image2.iterator();
			
			// si this n'est pas vide, la vider
			if (!isEmpty()) it.clear();
			
			explore_copy (it, it2, it2.getValue());
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Copie les noeuds d'un arbre dans un arbre vide
	 * 
	 * @param it
	 * 			iterateur sur l'arbre vide a remplir
	 * @param it2
	 * 			iterateur sur l'arbre a copier
	 * @param root
	 * 			noeud racine de l'arbre a copier
	 */
	private void explore_copy (Iterator<Node> it, Iterator<Node> it2, Node root) {
		
		// copier la valeur du noeud courant
		it.addValue(Node.valueOf(it2.getValue().state));
		
		// si le noeud a des fils, parcour recursif
		if (it2.nodeType() == NodeType.DOUBLE) {
			it2.goRight();
			it.goRight();
			explore_copy (it, it2, null);
			it2.goLeft();
			it.goLeft();
			explore_copy (it, it2, null);
		}
		
		// si le noeud n'est pas la racine, remonter au noeud parent
		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}
		
	}

	/**
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";
			
			Iterator<Node> it = iterator(),
						it2 = image2.iterator();
			
			if (!isEmpty()) it.clear();
			
			explore_rotate180 (it, it2, it2.getValue());
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void explore_rotate180 (Iterator<Node> it, Iterator<Node> it2, Node root) {
		
		it.addValue( Node.valueOf( it2.getValue().state));
		
		if (it2.nodeType() == NodeType.DOUBLE) {
			it.goLeft();
			it2.goRight();
			explore_rotate180 (it, it2, null);
			it.goRight();
			it2.goLeft();
			explore_rotate180 (it, it2, null);
		}
		
		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}
		
	}

	/**
	 * this devient rotation de image2 à 90 degrés dans le sens des aiguilles
	 * d'une montre.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate90(AbstractImage image2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction non demeand�e");
		System.out.println("-------------------------------------------------");
		System.out.println();	    
	}

	/**
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		try {
			assert (!isEmpty()) : "L'image est vide";
			Iterator<Node> it = iterator();
			if(this.numberOfNodes() == 1) {
				Node value = (it.getValue().state == 1) ? Node.valueOf(0) : Node.valueOf(1);
				it.setValue(value);
			} else {
				Node root = it.getValue();
				explore_switch (it, root);
			}
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Parcour l'arbre et inverse la valeur des feuilles
	 *  
	 * @param it
	 * 			iterateur sur l'arbre a parcourir
	 * @param root
	 * 			racine de l'arbre
	 */
	private void explore_switch (Iterator<Node> it, Node root) {
		System.out.println (it.getValue().state);
		if (it.nodeType() == NodeType.LEAF) {
			Node value = (it.getValue().state == 1) ? Node.valueOf(0) : Node.valueOf(1);
			it.setValue(value);
		} else {
			it.goRight();
			explore_switch (it, null);
			it.goLeft();
			explore_switch (it, null);
		}
		if(it.getValue() != root) it.goUp();
	}

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction � �crire");
		System.out.println("-------------------------------------------------");
		System.out.println();
	}

	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction � �crire");
		System.out.println("-------------------------------------------------");
		System.out.println();
	}

	/**
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";
			
			Iterator<Node> it = iterator(),
					it2 = image2.iterator();
			
			if (!isEmpty()) it.clear();
			
			// Cas d'une image pleine
			if (it2.nodeType() == NodeType.LEAF) affect (image2);
			
			// Cas d'une image dont la moitie superieure est pleine
			it2.goLeft();
			if (it2.nodeType() == NodeType.LEAF) it.addValue( Node.valueOf( it2.getValue().state));
			else {
				
				// Cas general
				it2.goLeft();
				explore_copy (it, it2, it2.getValue());
				
			}
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this
	 * devient éteint.
	 * 
	 * @param image2
	 *            image à réduire
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";
			
			Iterator<Node> it = iterator(),
					it2 = image2.iterator();
			
			if (!isEmpty()) it.clear();
			
			// la racine est un noeud double
			it.addValue( Node.valueOf(2));
			
			// eteindre la moitie inferieure de this
			it.goRight();
			it.addValue( Node.valueOf(0));
			it.goUp();
			
			// eteindre le quart superieur droit
			it.goLeft();
			it.addValue( Node.valueOf(2));
			it.goRight();
			it.addValue( Node.valueOf(0));
			it.goUp();
			
			// copier image2 dans le quart superieur gauche
			it.goLeft();
			explore_copy (it, it2, it2.getValue());
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * this devient l'intersection de image1 et image2 au sens des pixels
	 * allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction � �crire");
		System.out.println("-------------------------------------------------");
		System.out.println();
	}

	/**
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		try {
			assert (!isEmpty()) : "La premiere image est vide.";
			assert (!image2.isEmpty()) : "La seconde image est vide";
			
			Iterator<Node> it = iterator(),
					it2 = image2.iterator();
			
			explore_union (it, it2, it2.getValue());
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void explore_union (Iterator<Node> it, Iterator<Node> it2, Node root) {
		
		if (it.nodeType() == NodeType.LEAF) {
			if (it.getValue().state == 0)
				explore_copy (it, it2, it2.getValue());
		
		} else {
			
			if (it2.nodeType() == NodeType.LEAF) {
				if (it2.getValue().state == 1) {
					it.clear();
					it.addValue( Node.valueOf(1));
				}
			} else {
				
				it.goRight();
				it2.goRight();
				explore_union (it, it2, null);
				
				it.goLeft();
				it2.goLeft();
				explore_union (it, it2, null);
				
			}
			
		}
		
		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}
	
	}

	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	@Override
	public boolean testDiagonal() {
		boolean ret = false;
		
		if (!isEmpty()) {
			
			Iterator<Node> it = iterator();
			
			// cas d'une image pleine
			if (it.nodeType() == NodeType.LEAF)
				return it.getValue().state == 1;
			
			ret = explore_diagonal (it);
			
		}
	    return ret;
	}
	
	/**
	 * Teste si les pixels de la diagonale de la section d'image correspondant
	 * au noeud courant de it sont allumes.
	 * 
	 * @param it
	 * 			iterateur sur l'arbre a tester
	 * @return
	 * 			true si les pixels sont allumes,
	 * 			false sinon.
	 */
	private boolean explore_diagonal (Iterator<Node> it) {
		
		if (it.nodeType() == NodeType.LEAF)
			return it.getValue().state == 1;
		
		boolean ret;
		
		// tester la moitie superieure
		it.goLeft();
		
		// si la section est pleine
		if (it.nodeType() == NodeType.LEAF) {
			ret = it.getValue().state == 1;
			it.goUp();
			
		// tester le quart superieur gauche sinon
		} else {
			
			it.goLeft();
			ret = explore_diagonal (it);
			it.goUp();
			it.goUp();
			
		}
		
		// tester la moitie inferieure
		it.goRight();
		
		// si la section est pleine
		if (it.nodeType() == NodeType.LEAF) {
			ret = ret && it.getValue().state == 1;
			it.goUp();
		}
		
		// tester le quart inferieur droit sinon
		else {
			
			it.goRight();
			ret = ret && explore_diagonal (it);
			it.goUp();
			it.goUp();
			
		}
		
		return ret;
	}

	/**
	 * @param x1
	 *            abscisse du premier point
	 * @param y1
	 *            ordonnée du premier point
	 * @param x2
	 *            abscisse du deuxième point
	 * @param y2
	 *            ordonnée du deuxième point
	 * @pre !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par
	 *         la même feuille de this, false sinon
	 */
	@Override
	public boolean sameLeaf(int x1, int y1, int x2, int y2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction � �crire");
		System.out.println("-------------------------------------------------");
		System.out.println();
		return false;
	}

	/**
	 * @param image2
	 *            autre image
	 * @pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés
	 *         false sinon
	 */
	@Override
	public boolean isIncludedIn(AbstractImage image2) {
		try {
			assert (!isEmpty()) : "La premiere image est vide.";
			assert (!image2.isEmpty()) : "La seconde image est vide.";
			
			Iterator<Node> it = iterator(),
					it2 = image2.iterator();
			
			return explore_inclusion (it, it2, it2.getValue());
			
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}
	
	private boolean explore_inclusion (Iterator<Node> it, Iterator<Node> it2, Node root) {
		boolean ret = false, keepGoing = true;
		
		// une section vide est toujours incluse
		if (it.nodeType() == NodeType.LEAF) {
			ret = it.getValue().state == 0;
			keepGoing = false;
		}
		
		// si la section de image2 est pleine, elle ne peut contenir que si elle est allumee,
		// a moins que la section de this soit aussi vide
		if (it2.nodeType() == NodeType.LEAF) {
			ret = ret || it2.getValue().state == 1;
			keepGoing = false;
		}
		
		if (keepGoing) {
			it.goRight();
			it2.goRight();
			ret = explore_inclusion (it, it2, null);
			it.goLeft();
			it2.goRight();
			ret = ret && explore_inclusion (it, it2, null);
		}
		
		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}
		
		return ret;
	}

}
