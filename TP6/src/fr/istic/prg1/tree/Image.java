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
 *        Classe decrivant les images en noir et blanc de 256 sur 256 pixels
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
	 *            ordonnee du point
	 * @pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y) {
		try {
			assert (!isEmpty()) : "L'image est vide";
			Iterator<Node> it = iterator();
			explore_search(it, x, y, false, WINDOW_SIZE, WINDOW_SIZE);
			return it.getValue().state == 1;
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	/**
	 * Recherche le noeud de l'arbre contenant le point dont les coordonnees
	 * sont passees en parametre. Apres l'appel a cette fonction, ce noeud est
	 * le noeud courant de l'iterateur.
	 * 
	 * @param it
	 *            : iterateur sur l'arbre a parcourir
	 * @param x
	 *            : abscisse du point a rechercher
	 * @param y
	 *            : ordonnee du point a rechercher
	 * @param cutType
	 *            : sens du decoupage au noeud courant de it : true pour
	 *            vertical, false pour horizontal
	 * @param x_size
	 *            : largeur de la portion de fenetre correspondant au noeud
	 *            courant de it
	 * @param y_size
	 *            : hauteur de la portion de fenetre correspondant au noeud
	 *            courant de it
	 */
	private void explore_search(Iterator<Node> it, int x, int y, boolean cutType, int x_size, int y_size) {
		if (it.nodeType() == NodeType.DOUBLE) {

			// cas d'une coupe verticale
			if (cutType) {

				// calculer la largeur des sections suivantes
				x_size = x_size / 2;

				// cas d'une abscisse appartenant a la section gauche
				if (x < x_size) {
					it.goLeft();
					explore_search(it, x, y, !cutType, x_size, y_size);
				}
				// cas d'une abscisse appartenant a la section droite
				else {
					it.goRight();
					explore_search(it, x - x_size, y, !cutType, x_size, y_size);
				}

				// cas d'une coupe horizontale
			} else {

				// calculer la hauteur des sections suivantes
				y_size = y_size / 2;

				// cas d'une ordonnee appartenant a la section superieure
				if (y < y_size) {
					it.goLeft();
					explore_search(it, x, y, !cutType, x_size, y_size);
					// cas d'une ordonnee appartenant a la section inferieure
				} else {
					it.goRight();
					explore_search(it, x, y - y_size, !cutType, x_size, y_size);
				}
			}
		}
	}

	/**
	 * this devient identique a image2.
	 *
	 * @param image2
	 *            image a copier
	 *
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			// si this n'est pas vide, la vider
			if (!isEmpty())
				it.clear();

			explore_copy(it, it2, it2.getValue());

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Copie les noeuds d'un arbre dans un arbre vide
	 * 
	 * @param it
	 *            iterateur sur l'arbre vide a remplir
	 * @param it2
	 *            iterateur sur l'arbre a copier
	 * @param root
	 *            noeud racine de l'arbre a copier
	 */
	private void explore_copy(Iterator<Node> it, Iterator<Node> it2, Node root) {

		// copier la valeur du noeud courant
		it.addValue(Node.valueOf(it2.getValue().state));

		// si le noeud a des fils, parcour recursif
		if (it2.nodeType() == NodeType.DOUBLE) {
			it2.goRight();
			it.goRight();
			explore_copy(it, it2, null);
			it2.goLeft();
			it.goLeft();
			explore_copy(it, it2, null);
		}

		// si le noeud n'est pas la racine, remonter au noeud parent
		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}

	}

	/**
	 * this devient rotation de image2 a 180 degres.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			explore_rotate180(it, it2, it2.getValue());

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void explore_rotate180(Iterator<Node> it, Iterator<Node> it2, Node root) {

		it.addValue(Node.valueOf(it2.getValue().state));

		if (it2.nodeType() == NodeType.DOUBLE) {
			it.goLeft();
			it2.goRight();
			explore_rotate180(it, it2, null);
			it.goRight();
			it2.goLeft();
			explore_rotate180(it, it2, null);
		}

		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}

	}

	/**
	 * this devient rotation de image2 a 90 degres dans le sens des aiguilles
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
	 * this devient inverse video de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		try {
			assert (!isEmpty()) : "L'image est vide";
			Iterator<Node> it = iterator();
			if (this.numberOfNodes() == 1) {
				Node value = (it.getValue().state == 1) ? Node.valueOf(0) : Node.valueOf(1);
				it.setValue(value);
			} else {
				Node root = it.getValue();
				explore_switch(it, root);
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
	 *            iterateur sur l'arbre a parcourir
	 * @param root
	 *            racine de l'arbre
	 */
	private void explore_switch(Iterator<Node> it, Node root) {
		System.out.println(it.getValue().state);
		if (it.nodeType() == NodeType.LEAF) {
			Node value = (it.getValue().state == 1) ? Node.valueOf(0) : Node.valueOf(1);
			it.setValue(value);
		} else {
			it.goRight();
			explore_switch(it, null);
			it.goLeft();
			explore_switch(it, null);
		}
		if (it.getValue() != root)
			it.goUp();
	}

	/**
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2
	 *            image a agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image2) {

		try {
			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			copy_mirrorV(it, it2, it2.getValue(), true);

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	private void copy_mirrorV(Iterator<Node> it, Iterator<Node> it2, Node root, boolean cut) {

		it.addValue(Node.valueOf(it2.getValue().state));

		if (it2.nodeType() == NodeType.DOUBLE) {

			// cas d'une coupe horizontale : copier le fils droit du noeud
			// courant de it2
			// comme fils gauche du noeud courant de it
			if (cut) {

				it.goLeft();
				it2.goRight();
				copy_mirrorV(it, it2, null, !cut);

				// vice-versa
				it.goRight();
				it2.goLeft();
				copy_mirrorV(it, it2, null, !cut);

				// cas d'une coupe verticale, copie
			} else {

				it.goLeft();
				it2.goLeft();
				copy_mirrorV(it, it2, null, !cut);

				it.goRight();
				it2.goRight();
				copy_mirrorV(it, it2, null, !cut);

			}

		}

		if (it2.getValue() != root) {
			it.goUp();
			it2.goUp();
		}

	}

	/**
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2
	 *            : image origine de la symetrie
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image2) {

		try {

			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			copy_mirrorH(it, it2, it2.getValue(), false);

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	private void copy_mirrorH(Iterator<Node> it, Iterator<Node> it2, Node root, boolean cut) {

		it.addValue(Node.valueOf(it2.getValue().state));

		if (it2.nodeType() == NodeType.DOUBLE) {

			// cas d'une coupe verticale : copier le fils droit du noeud courant
			// de it2
			// comme fils gauche du noeud courant de it
			if (cut) {

				it.goLeft();
				it2.goRight();
				copy_mirrorH(it, it2, null, !cut);

				// vice-versa
				it.goRight();
				it2.goLeft();
				copy_mirrorH(it, it2, null, !cut);

				// cas d'une coupe horizontale, copie
			} else {

				it.goLeft();
				it2.goLeft();
				copy_mirrorH(it, it2, null, !cut);

				it.goRight();
				it2.goRight();
				copy_mirrorH(it, it2, null, !cut);
			}

		}

		if (it2.getValue() != root) {
			it.goUp();
			it2.goUp();
		}
	}

	/**
	 * this devient quart superieur gauche de image2.
	 *
	 * @param image2
	 *            image a agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			// Cas d'une image pleine
			if (it2.nodeType() == NodeType.LEAF)
				affect(image2);

			// Cas d'une image dont la moitie superieure est pleine
			it2.goLeft();
			if (it2.nodeType() == NodeType.LEAF)
				it.addValue(Node.valueOf(it2.getValue().state));
			else {

				// Cas general
				it2.goLeft();
				explore_copy(it, it2, it2.getValue());

			}

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Le quart superieur gauche de this devient image2, le reste de this
	 * devient eteint.
	 * 
	 * @param image2
	 *            image a� reduire
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) {
		try {
			assert (!image2.isEmpty()) : "L'image est vide.";

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			// la racine est un noeud double
			it.addValue(Node.valueOf(2));

			// eteindre la moitie inferieure de this
			it.goRight();
			it.addValue(Node.valueOf(0));
			it.goUp();

			// eteindre le quart superieur droit
			it.goLeft();
			it.addValue(Node.valueOf(2));
			it.goRight();
			it.addValue(Node.valueOf(0));
			it.goUp();

			// copier image2 dans le quart superieur gauche
			it.goLeft();
			copy_zoomOut (it, it2, 0, it2.getValue());
			
			// valider la moitie superieure de l'image
			it.goUp();
			validate_node (it);
			
			// valider l'image
			it.goUp();
			validate_node (it);

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void copy_zoomOut (Iterator<Node> it, Iterator<Node> it2, int depth, Node root) {
		
		if (depth < 14) {
			
			it.addValue (Node.valueOf (it2.getValue().state));
			
			if (it2.nodeType() == NodeType.DOUBLE) {
				
				it.goLeft();
				it2.goLeft();
				copy_zoomOut (it, it2, depth + 1, null);
				
				it.goRight();
				it2.goRight();
				copy_zoomOut (it, it2, depth + 1, null);
				
				validate_node (it);
				
			}
			
		} else
			copy_shrink (it, it2);
		
		if (it2.getValue() != root) {
			it.goUp();
			it2.goUp();
		}
		
	}

	private void copy_shrink (Iterator<Node> it, Iterator<Node> it2) {

		// cas d'un sous-arbre de hauteur 2 :
		// remplacer par une feuille de valeur egale a sa valeur moyenne
		if (xHeight (it2) == 2) {
			
			int v = average_value (it2);
			it.addValue (Node.valueOf (v));

		// cas d'un sous-arbre de hauteur 1 :
		// forcement de la forme 2 -> (1 0), remplacer par une feuille de valeur 1
		} else if (xHeight (it2) == 1)
			it.addValue (Node.valueOf (1));
		
		else
			it.addValue (Node.valueOf (it2.getValue().state));

	}

	/**
	 * Calcule la valeur moyenne des feuilles dans un sous-arbre de hauteur <= 2
	 * 
	 * @param it
	 * @return
	 */
	private int average_value(Iterator<Node> it) {
		int ret = 0;

		// un sous-arbre de hauteur 2 contenant 7 noeuds est forcement de la forme
		// 2 -> ( [ 2 -> (1 0)] ; [ 2 -> (1 0) ] )
		if (xNumberOfNodes (it) == 7)
			return 1;

		// cas d'un sous-arbre de la forme 
		// 2 -> ( [ 2 -> (1 0)] ; x )
		// la valeur moyenne sera celle de x
		
		it.goLeft();
		if (it.nodeType() == NodeType.LEAF) ret = it.getValue().state;
		else {
			it.goUp();
			it.goRight();
			ret = it.getValue().state;
		}
		it.goUp();
		
		return ret;
	}

	/**
	 * this devient l'intersection de image1 et image2 au sens des pixels
	 * allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1
	 *            premiere image
	 * @param image2
	 *            seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {
		try {
			assert (!(image1.isEmpty() || image2.isEmpty())) : "Les deux images sont vides";
			assert (image1 != this) : "image1 == this";
			assert (image2 != this) : "image2 == this";

			Iterator<Node> it1 = image1.iterator(), it2 = image2.iterator(), it3 = this.iterator();

			it3.clear();

			explore_intersection(it1, it2, it3, it1.getValue(), it2.getValue(), it3.getValue());
		} catch (AssertionError e) {

		}
	}

	/**
	 * Insere a la position courante de it1 le sous-arbre resultant de
	 * l'intersection du sous-arbre reference par le noeud courant de it2 et du
	 * sous-arbre reference par le noeud courant de i3
	 * 
	 * @param it1
	 *            : iterateur sur this
	 * @param it2
	 *            : iterateur sur image1
	 * @param it3
	 *            : iterateur sur image2
	 * @param root1
	 *            : racine de this
	 * @param root2
	 *            : racine de image1
	 * @param root3
	 *            : racine de image3
	 */
	public void explore_intersection(Iterator<Node> it1, Iterator<Node> it2, Iterator<Node> it3, Node root1, Node root2,
			Node root3) {

		// cas d'arret
		if (it1.nodeType() == NodeType.LEAF) {
			// une feuille de valeur 1 est un element neutre de l'intersection,
			// copier le noeud de image2
			if (it1.getValue().state == 1)
				explore_copy(it3, it2, it2.getValue());
			// une feuille de valeur 0 est un element absorbant de
			// l'intersection,
			// inserer une feuille de valeur 0
			else
				it3.addValue(Node.valueOf(0));

		} else if (it2.nodeType() == NodeType.LEAF) {

			if (it2.getValue().state == 1)
				explore_copy(it3, it1, it1.getValue());
			else
				it3.addValue(Node.valueOf(0));

			// continuer le parcours dans le cas ou les deux noeuds sont
			// doubles,
			// et ajouter un noeud double a this
		} else {
			it3.addValue(Node.valueOf(2));
			it1.goRight();
			it2.goRight();
			it3.goRight();
			explore_intersection(it1, it2, it3, null, null, null);

			it1.goLeft();
			it2.goLeft();
			it3.goLeft();
			explore_intersection(it1, it2, it3, null, null, null);

			// verifier que le noeud insere est valide
			validate_node(it3);

		}

		if (it1.getValue() != root1) {
			it1.goUp();
			it2.goUp();
			it3.goUp();
		}
	}

	/**
	 * Valide le noeud courant de it : le reduit a une feuille lorsque ses deux
	 * fils sont des feuilles de meme valeur
	 * 
	 * @pre : le noeud courant est double
	 * 
	 * @param it
	 *            : iterateur pointant sur le noeud a valider
	 */
	private void validate_node(Iterator<Node> it) {
		try {
			assert (it.nodeType() == NodeType.DOUBLE) : "Impossible de reduire une feuille";

			int v = 2;

			// lire la valeur du fils gauche si c'est une feuille
			it.goLeft();
			if (it.nodeType() == NodeType.LEAF)
				v = it.getValue().state;
			it.goUp();

			// si le fils gauche etait une feuille, tester le fils droit
			if (v != 2) {
				it.goRight();

				if (it.nodeType() == NodeType.LEAF) {
					// si c'est une feuille de meme valeur, remplacer le
					// noeud a valider par une feuille de valeur v
					if (it.getValue().state == v) {
						it.goUp();
						it.clear();
						it.addValue(Node.valueOf(v));
					} else
						it.goUp();
				} else
					it.goUp();
			}
		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1
	 *            premiere image
	 * @param image2
	 *            seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		try {
			assert (!image1.isEmpty()) : "La premiere image est vide.";
			assert (!image2.isEmpty()) : "La seconde image est vide";

			Iterator<Node> it = iterator(), it1 = image1.iterator(), it2 = image2.iterator();

			if (!isEmpty())
				it.clear();

			explore_union(it, it1, it2, it2.getValue());

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void explore_union(Iterator<Node> it, Iterator<Node> it1, Iterator<Node> it2, Node root) {

		// cas d'arret

		// si le noeud courant de image1 est une feuille :
		if (it1.nodeType() == NodeType.LEAF) {
			// une feuille de valeur 0 est un element neutre de l'union,
			// copier le noeud courant de image2
			if (it1.getValue().state == 0)
				explore_copy(it, it2, it2.getValue());
			// une feuille de valeur 1 est un element absorbant de l'union,
			// ajouter une feuille de valeur 1 a this
			else
				it.addValue(Node.valueOf(1));

		} else if (it2.nodeType() == NodeType.LEAF) {
			if (it2.getValue().state == 0)
				explore_copy(it, it1, it1.getValue());
			else
				it.addValue(Node.valueOf(1));

			// continuer le parcour si les deux noeuds sont doubles
		} else {

			it.addValue(Node.valueOf(2));

			it.goRight();
			it1.goRight();
			it2.goRight();
			explore_union(it, it1, it2, null);

			it.goLeft();
			it1.goLeft();
			it2.goLeft();
			explore_union(it, it1, it2, null);

			// verifier que le noeud insere est valide
			validate_node(it);
		}

		if (!it2.getValue().equals(root)) {
			it.goUp();
			it1.goUp();
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

			ret = explore_diagonal(it);

		}
		return ret;
	}

	/**
	 * Teste si les pixels de la diagonale de la section d'image correspondant
	 * au sous-arbre reference par le noeud courant de it sont allumes.
	 * 
	 * @param it
	 *            : iterateur sur l'arbre a tester
	 * 
	 * @return : true si les pixels sont allumes, false sinon.
	 */
	private boolean explore_diagonal(Iterator<Node> it) {

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
			ret = explore_diagonal(it);
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
			ret = ret && explore_diagonal(it);
			it.goUp();
			it.goUp();

		}

		return ret;
	}

	/**
	 * @param x1
	 *            abscisse du premier point
	 * @param y1
	 *            ordonnee du premier point
	 * @param x2
	 *            abscisse du deuxième point
	 * @param y2
	 *            ordonnee du deuxième point
	 * 
	 * @pre !this.isEmpty()
	 * 
	 * @return true si les deux points (x1, y1) et (x2, y2) sont representes par
	 *         la meme feuille de this, false sinon
	 */
	@Override
	public boolean sameLeaf(int x1, int y1, int x2, int y2) {
		boolean ret = false;
		try {
			assert (!isEmpty()) : "L'image est vide";

			Iterator<Node> it = iterator();

			ret = explore_sameLeaf(it, x1, y1, x2, y2, false, WINDOW_SIZE, WINDOW_SIZE);

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}

		return ret;
	}

	/**
	 * Parcour le sous-arbre reference par le noeud courant de it afin d'etablir
	 * si les points de coordonnes (x1,y1) et (x2,y2) appartiennent a la meme
	 * feuille.
	 * 
	 * @param it
	 *            : iterateur sur l'arbre a parcourir
	 * @param x1
	 *            : abscisse du premier point
	 * @param y1
	 *            : ordonnee du premier point
	 * @param x2
	 *            : ordonnee du second point
	 * @param y2
	 *            : ordonnee du second point
	 * @param cutType
	 *            : sens du decoupage au noeud courant de it : true pour
	 *            vertical, false pour horizontal
	 * @param x_size
	 *            : largeur de la portion de fenetre correspondant au noeud
	 *            courant de it
	 * @param y_size
	 *            : hauteur de la portion de fenetre correspondant au noeud
	 *            courant de it
	 */
	private boolean explore_sameLeaf(Iterator<Node> it, int x1, int y1, int x2, int y2, boolean cut, int x_size,
			int y_size) {

		if (it.nodeType() == NodeType.LEAF)
			return true;

		// cas d'une coupe verticale
		if (cut) {

			// calculer la largeur de la section suivante
			x_size = x_size / 2;

			// parcour
			if (x1 < x_size && x2 < x_size) {
				it.goLeft();
				return explore_sameLeaf(it, x1, y1, x2, y2, !cut, x_size, y_size);
			}

			if (x1 >= x_size && x2 >= x_size) {
				it.goRight();
				return explore_sameLeaf(it, x1 - x_size, y1, x2 - x_size, y2, !cut, x_size, y_size);
			}

			return false;

			// cas d'une coupe horizontale
		} else {

			// calculer la hauteur de la section suivante
			y_size = y_size / 2;

			// parcour
			if (y1 < y_size && y2 < y_size) {
				it.goLeft();
				return explore_sameLeaf(it, x1, y1, x2, y2, !cut, x_size, y_size);
			}

			if (y1 >= y_size && y2 >= y_size) {
				it.goRight();
				return explore_sameLeaf(it, x1, y1 - y_size, x2, y2 - y_size, !cut, x_size, y_size);
			}

			return false;

		}

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

			Iterator<Node> it = iterator(), it2 = image2.iterator();

			return explore_inclusion(it, it2, it2.getValue());

		} catch (AssertionError e) {
			e.printStackTrace();
			System.exit(0);
		}
		return false;
	}

	/**
	 * Teste si le sous-arbre reference par le noeud courant de it est inclus
	 * dans le sous-arbre reference par le noeud courant de it2, au sens des
	 * pixel allumes
	 * 
	 * @param it
	 *            : iterateur sur le sous-arbre de this
	 * @param it2
	 *            : iterateur sur le sous-arbre de image2
	 * @param root
	 *            : noeud racine de image2
	 * 
	 * @return : true si le sous-arbre de this est inclus dans le sous-arbre de
	 *         image2, au sens des pixel allumes
	 */
	private boolean explore_inclusion(Iterator<Node> it, Iterator<Node> it2, Node root) {
		boolean ret = false, keepGoing = true;

		// une section vide est toujours incluse
		if (it.nodeType() == NodeType.LEAF) {
			ret = it.getValue().state == 0;
			keepGoing = false;
		}

		// si la section de image2 est une feuille,
		// elle ne peut contenir la section de this que si elle est allumee,
		// a moins que la section de this soit aussi vide
		if (it2.nodeType() == NodeType.LEAF) {
			ret = ret || it2.getValue().state == 1;
			keepGoing = false;
		}

		// si les deux noeuds sont doubles, continuer le parcour
		if (keepGoing) {
			it.goRight();
			it2.goRight();
			ret = explore_inclusion(it, it2, null);

			it.goLeft();
			it2.goLeft();
			ret = ret && explore_inclusion(it, it2, null);
		}

		if (!it2.getValue().equals(root)) {
			it.goUp();
			it2.goUp();
		}

		return ret;
	}

}
