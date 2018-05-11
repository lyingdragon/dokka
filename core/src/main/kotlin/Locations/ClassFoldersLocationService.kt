//lyingdragon
package org.jetbrains.dokka

import com.google.inject.Inject
import com.google.inject.name.Named
import java.io.File

class ClassFoldersLocationService @Inject constructor(@Named("outputDir") val rootFile: File, val extension: String) : FileLocationService {
    constructor(root: String): this(File(root), "")

    override val root: Location
        get() = FileLocation(rootFile)

    override fun withExtension(newExtension: String): FileLocationService {
        return if (extension.isEmpty()) ClassFoldersLocationService(rootFile, newExtension) else this
    }

    override fun location(qualifiedName: List<String>, hasMembers: Boolean): FileLocation {
        //lyingdragon
        /* println("dragon: qualifiedName[0]= " + qualifiedName.get(0) + ", has members= " + hasMembers)
        println("dragon: root= " + rootFile + ", relativePathToNode(qualifiedName, hasMembers))= " + relativePathToNode(qualifiedName, hasMembers)) */

        return FileLocation(File(rootFile, relativePathToNode(qualifiedName, hasMembers)).appendExtension(extension))
    }

    //lyingdragon
    override fun location(node: DocumentationNode): FileLocation {
      println("dragon: name= "+ node.qualifiedName() + ", kind= " + node.kind) // !in NodeKind.classLike
      /* if( node.kind in NodeKind.memberLike ) {
        return
      }
      else  */
        return super.location(node)
    }

    //lyingdragon
    fun relativePathToNode(qualifiedName: List<String>, hasMembers: Boolean): String {
        var parts = qualifiedName.map { identifierToFilename(it) }.filterNot { it.isEmpty() }
        return if (!hasMembers) {
            // leaf node, use file in owner's folder
            parts = parts.dropLast(1)
            //parts.joinToString("/")
            parts.joinToString("/") + (if (parts.none()) "" else "/") + "memebers"
        } else {
            parts.joinToString("/") + (if (parts.none()) "" else "/") + "index"
        }
    }
}
