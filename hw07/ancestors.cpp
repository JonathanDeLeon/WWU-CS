/****************************************************************************
 *
 * Howemork.06: Implement a three functions for printing tree ancestors.
 *
 * File Name:  ancestors.cpp
 * Name:       Jonathan De Leon
 * Course:     CPTR 242
 * Date:       5/28/18
 * Time Taken: 1 hr
 *
 */
#include "TreeType.h"

void TreeType::AncestorsIterative(ItemType value, std::ofstream& outFile) {
    TreeNode* tree = root;
    while (tree != NULL && tree->info != value) {
        outFile << tree->info;
        if (value < tree->info) {
            tree = tree->left;
        } else if (value > tree->info) {
            tree = tree->right;
        }
    }
}

void PrintAncestorsRecursive(TreeNode* tree, ItemType value, std::ofstream& outFile) {
    if (tree == NULL || tree->info == value) {
        return;
    }
    outFile << tree->info;
    if (value < tree->info) {
        PrintAncestorsRecursive(tree->left, value, outFile);
    } else if (value > tree->info) {
        PrintAncestorsRecursive(tree->right, value, outFile);
    }
}

void PrintAncestorsReverse(TreeNode* tree, ItemType value, std::ofstream& outFile) {
    if (tree == NULL || tree->info == value) {
        return;
    }
    if (value < tree->info) {
        PrintAncestorsReverse(tree->left, value, outFile);
    } else if (value > tree->info) {
        PrintAncestorsReverse(tree->right, value, outFile);
    }
    outFile << tree->info;
}